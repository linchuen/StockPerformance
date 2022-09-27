package cooba.stockPerformance.Utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class HttpUtil {
    @Autowired
    OkHttpClient okHttpClient;

    @Autowired
    ObjectMapper objectMapper;

    public <T> Optional<T> httpGet(String url, Map<String, String> param, Class<T> clazz) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        param.forEach(urlBuilder::addQueryParameter);
        HttpUrl httpUrl = urlBuilder.build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        Call call = okHttpClient.newCall(request);

        try {
            Response response = call.execute();
            return Optional.of(objectMapper.readValue(Objects.requireNonNull(response.body()).string(), clazz));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public Response httpGet(String url, Map<String, String> param) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        param.forEach(urlBuilder::addQueryParameter);
        HttpUrl httpUrl = urlBuilder.build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        Call call = okHttpClient.newCall(request);

        try {
            Response response = call.execute();
            return response;
        } catch (IOException e) {
            return null;
        }
    }
}
