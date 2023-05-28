import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.util.function.Consumer;

public class StockMarketBot {
    public static String chatGPT(String text, Consumer<Double> progressCallback) throws Exception {
        String url = "https://api.openai.com/v1/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer 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");

        JSONObject data = new JSONObject();
        data.put("model", "text-davinci-003");
        data.put("prompt", text);
        data.put("max_tokens", 4000);
        data.put("temperature", 0.3);

        con.setDoOutput(true);
        con.getOutputStream().write(data.toString().getBytes());

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();
        for (double progress = 0; progress < 1; progress += 0.1) {
        progressCallback.accept(progress);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    progressCallback.accept(1.0);

        return (new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text"));
    }
    
}
