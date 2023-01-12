package cooba.stockPerformance.Utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class HttpUtil {
    @Autowired
    OkHttpClient okHttpClient;

    @Autowired
    ObjectMapper objectMapper;

    public <T> Optional<T> httpGet(String url, Map<String, String> param, Map<String, String> header, Class<T> clazz) {
        try {
            Response response = httpGet(url, param, header);
            return Optional.of(objectMapper.readValue(Objects.requireNonNull(response.body()).string(), clazz));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public Response httpGet(String url, Map<String, String> param, Map<String, String> header) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        param.forEach(urlBuilder::addQueryParameter);
        HttpUrl httpUrl = urlBuilder.build();

        Request.Builder requestBuilder = new Request.Builder()
                .url(httpUrl);

        header.forEach(requestBuilder::addHeader);

        Call call = okHttpClient.newCall(requestBuilder.build());

        try {
            return call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Response httpGet(String url, Map<String, String> param) {
        return httpGet(url, param, Collections.emptyMap());
    }
}
