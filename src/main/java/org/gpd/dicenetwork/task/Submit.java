//package org.gpd.dicenetwork.task;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//
//@Service
//public class Submit {
//    @Value("${submit.requestUrl}")
//    private String requestUrl;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    public ApiResponse<Void> submitLotteryHash(SubmitLotteryHashRequest request) {
//        try{
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(request), headers);
//
//            ResponseEntity<String> responseEntity = restTemplate.exchange(
//                    requestUrl,
//                    HttpMethod.POST,
//                    entity,
//                    String.class
//            );
//            System.out.println(responseEntity.getBody());
//            if (responseEntity.getStatusCode().is2xxSuccessful()) {
//                try {
//                    return objectMapper.readValue(responseEntity.getBody(), ApiResponse.class);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    // 处理解析错误，可以返回一个默认的响应或抛出异常
//                    return new ApiResponse<>("Error parsing response", 500, null);
//                }
//            } else {
//                // 处理非2xx响应
//                return new ApiResponse<>("Request failed with status code: " + responseEntity.getStatusCodeValue(),
//                        responseEntity.getStatusCodeValue(), null);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ApiResponse<>("Error parsing request", 400, null);
//        }
//    }
//
//}
