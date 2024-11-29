package org.gpd.dicenetwork.nettyServer;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientManagerService {

    private static ConcurrentMap<String, Channel> clients = new ConcurrentHashMap<>();

    // 添加客户端
    public void addClient(String clientName, Channel channel) {
        clients.put(clientName, channel);
    }

    // 移除客户端
    public void removeClientByName(String clientName) {
        clients.remove(clientName);
    }

    public void removeClient(Channel channel) {
        clients.remove(channel);
    }

    public static String findKeyByChannel(Channel channel) {
        for (Map.Entry<String, Channel> entry : clients.entrySet()) {
            if (entry.getValue().equals(channel)) {
                return entry.getKey();
            }
        }
        return null;
    }

    // 广播消息给所有客户端
    public void broadcastMessage(String message) {
        clients.forEach((clientName, channel) -> {
            if (channel.isActive()) {
                channel.writeAndFlush(message); // 假设您已经配置了合适的ByteBuf或类似的数据结构
            }
        });
    }

    // 提供给外部访问，以便发送消息给所有客户端
    public void sendMessageToAllClients(String message) {
        clients.forEach((clientName, channel) -> {
            if (channel.isActive()) {
                channel.writeAndFlush(message);
//                channel.writeAndFlush(Unpooled.copiedBuffer(message.getBytes()));
            }
        });
    }

    // 根据客户端名称发送消息
    public boolean sendMessageToClient(String clientName, String message) {
        Channel channel = clients.get(clientName);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(message);
            return true;
        } else {
            log.info("Client " + clientName + " is not active or does not exist.");
            return false;
        }
    }

    // 获取客户端数量（可选）
    public int getClientCount() {
        return clients.size();
    }
}