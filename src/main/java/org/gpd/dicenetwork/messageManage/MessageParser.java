package org.gpd.dicenetwork.messageManage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class MessageParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 创建一个映射，用于存储messageName和对应的bodyType
    private static final Map<String, Class<?>> bodyTypeMap = new HashMap<>();

    static {
        bodyTypeMap.put("loginVqr", LoginVqrEntity.class);
        bodyTypeMap.put("lotteryStart", LotteryStartEntity.class);
        bodyTypeMap.put("lotteryResult", LotteryResultEntity.class);
        bodyTypeMap.put("luckyWallet", LuckyWalletEntity.class);
        bodyTypeMap.put("registerWallet", RegisterWalletEntity.class);
        bodyTypeMap.put("getLotteryTime", GetLotteryTimeEntity.class);
    }

    /**
     * 解析JSON字符串为Message对象，自动判断body类型
     *
     * @param jsonString JSON格式的消息字符串
     * @return 解析后的Message对象
     */
    public static synchronized MessageEntity<?> parseMessage(String jsonString) throws JsonProcessingException, IllegalArgumentException {

        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);

            MessageHeaderEntity header = objectMapper.treeToValue(rootNode.get("header"), MessageHeaderEntity.class);
            String messageName = header.getMessageName().name();

            JsonNode bodyNode = rootNode.get("body");
            int actualBodyLength = bodyNode.toString().length();
            int declaredLength = header.getMessageLength();
            if (actualBodyLength != declaredLength) {
                System.out.println("MessageLength:" + actualBodyLength + " ### Bodylength:" + declaredLength);
//                throw new IllegalArgumentException("Message length is wrong!");
            }

            Class<?> bodyType = getBodyTypeByMessageName(messageName);
            Object body = objectMapper.treeToValue(bodyNode, bodyType);

            return new MessageEntity<>(header, body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Parse message is failed!", e);
        }

    }

    /**
     * 根据消息名称获取对应的body类
     *
     * @param messageName 消息名称
     * @return 对应的body类
     */
    private static Class<?> getBodyTypeByMessageName(String messageName) {
        Class<?> bodyType = bodyTypeMap.get(messageName);
        if (bodyType == null) {
            throw new IllegalArgumentException("未知的消息名称: " + messageName);
        }
        return bodyType;
    }

    /**
     * 注册新的消息类型
     *
     * @param messageName 消息名称
     * @param bodyType    对应的body类
     */
    public static synchronized void registerMessageType(String messageName, Class<?> bodyType) {
        bodyTypeMap.put(messageName, bodyType);
    }


    public static synchronized String createMessageEntityJson(MessageName messageName, Object body, String checksum) {
        try {
            MessageHeaderEntity header = new MessageHeaderEntity(
                    0,
                    MessageType.response,
                    messageName,
                    "1.0",
                    checksum);

            MessageEntity<?> messageEntity = new MessageEntity<>(header, body);

            // 使用 ObjectMapper 将整个 MessageEntity 转换为 JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(messageEntity);

            // 解析 JSON 以更新消息长度
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode bodyNode = rootNode.get("body");
            System.out.println("bodyNode：" + bodyNode.toString());
            int bodyLength = bodyNode.toString().length();
//            System.out.println("body：" + bodyNode.toString() + "，长度:" + bodyLength);

            // 更新消息头中的长度
            ((ObjectNode) rootNode.get("header")).put("messageLength", bodyLength);

            // 重新生成 JSON 字符串
            return rootNode.toString();
        } catch (JsonProcessingException e) {
            log.error("An error occurred while creating the message entity JSON", e);
            return null;
        }
    }
}
