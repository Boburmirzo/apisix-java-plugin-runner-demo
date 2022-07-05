package org.apache.apisix.plugin.runner.filter;

import com.google.gson.Gson;
import org.apache.apisix.plugin.runner.HttpRequest;
import org.apache.apisix.plugin.runner.HttpResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TokenValidator implements PluginFilter {

    @Override
    public String name() {
        return "TokenValidator";
    }

    @Override
    public void filter(HttpRequest request, HttpResponse response, PluginFilterChain chain) {
        // parse `conf` to json
        String configStr = request.getConfig(this);
        Gson gson = new Gson();
        Map<String, Object> conf = new HashMap<>();
        conf = gson.fromJson(configStr, conf.getClass());

        // get configuration parameters
        String token = request.getHeader((String) conf.get("validate_header"));
        String validate_url = (String) conf.get("validate_url");
        boolean flag = validate(token, validate_url);

        // token verification results
        if (!flag) {
            String rejected_code = (String) conf.get("rejected_code");
            response.setStatusCode(Integer.parseInt(rejected_code));
            chain.filter(request, response);
            return;
        }

        chain.filter(request, response);
    }

    @Override
    public List<String> requiredVars() {
        return null;
    }

    @Override
    public Boolean requiredBody() {
        return null;
    }

    private Boolean validate(String token, String validate_url) {
        //TODO: improve the validation process
        return true;
    }
}
