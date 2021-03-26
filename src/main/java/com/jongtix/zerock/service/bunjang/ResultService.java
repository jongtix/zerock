package com.jongtix.zerock.service.bunjang;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jongtix.zerock.domain.bunjang.Event;
import com.jongtix.zerock.domain.bunjang.Result;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ResultService {

    Map<String, Result> resultMap = new HashMap<>();

    public String getList(String sort) {

        List<Result> valueList = new ArrayList<>(resultMap.values());

        Comparator<Result> comparator = getComparator(sort);
        valueList.sort(comparator);

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(valueList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Comparator<Result> getComparator(String sort) {

        Comparator<Result> comparator = (s1, s2) -> {

            Integer temp1 = 0;
            Integer temp2 = 0;

            if (s1.getEvents().get(sort) != null) {
                temp1 = s1.getEvents().get(sort);
            }
            if (s2.getEvents().get(sort) != null) {
                temp2 = s2.getEvents().get(sort);
            }

            return temp2.compareTo(temp1);
        };

        return comparator;
    }

    public void service() {
//        String url = "https://*****************/bunjang-interview/events.json";
//        String result = resultGetMethod(url);
        String result = "[{\"id\":\"8594176318\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":6443532,\"login\":\"bogdanvlviv\"}},{\"id\":\"8594070315\",\"type\":\"WatchEvent\",\"actor\":{\"id\":40355304,\"login\":\"mostrozny\"}},{\"id\":\"8594026559\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":374824,\"login\":\"hult\"}},{\"id\":\"8593921688\",\"type\":\"WatchEvent\",\"actor\":{\"id\":40750351,\"login\":\"JPatsushi\"}},{\"id\":\"8593579061\",\"type\":\"ForkEvent\",\"actor\":{\"id\":45061786,\"login\":\"jacobpowlett67\"}},{\"id\":\"8593471860\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":16337910,\"login\":\"vtm9\"}},{\"id\":\"8593447505\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":415169,\"login\":\"libin0120\"}},{\"id\":\"8593343968\",\"type\":\"WatchEvent\",\"actor\":{\"id\":2605791,\"login\":\"Lewiscowles1986\"}},{\"id\":\"8593343123\",\"type\":\"ForkEvent\",\"actor\":{\"id\":28643518,\"login\":\"krenoirw\"}},{\"id\":\"8593312073\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":24282,\"login\":\"lacostej\"}},{\"id\":\"8592989881\",\"type\":\"WatchEvent\",\"actor\":{\"id\":29328641,\"login\":\"ajinkyag911\"}},{\"id\":\"8592902347\",\"type\":\"ForkEvent\",\"actor\":{\"id\":1285489,\"login\":\"sjorz51\"}},{\"id\":\"8592874209\",\"type\":\"WatchEvent\",\"actor\":{\"id\":38899256,\"login\":\"monachi\"}},{\"id\":\"8592865223\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":28418009,\"login\":\"rails-bot[bot]\"}},{\"id\":\"8592609115\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":4415419,\"login\":\"janwerkhoven\"}},{\"id\":\"8592445717\",\"type\":\"WatchEvent\",\"actor\":{\"id\":15173574,\"login\":\"Nytorian\"}},{\"id\":\"8592443256\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":140718,\"login\":\"gaganawhad\"}},{\"id\":\"8592229564\",\"type\":\"IssuesEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8592219951\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8592214995\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8592181459\",\"type\":\"IssuesEvent\",\"actor\":{\"id\":1550934,\"login\":\"brentkearney\"}},{\"id\":\"8592145761\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":10163,\"login\":\"kpumuk\"}},{\"id\":\"8592094137\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":5162312,\"login\":\"gmcgibbon\"}},{\"id\":\"8592074962\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8592068649\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8592055635\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":1080678,\"login\":\"eileencodes\"}},{\"id\":\"8592043137\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":10163,\"login\":\"kpumuk\"}},{\"id\":\"8591717238\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8591675738\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":7468109,\"login\":\"rails-bot\"}},{\"id\":\"8591675469\",\"type\":\"PullRequestEvent\",\"actor\":{\"id\":6443532,\"login\":\"bogdanvlviv\"}},{\"id\":\"8591586524\",\"type\":\"PushEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8591574235\",\"type\":\"PullRequestEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8591565875\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":671550,\"login\":\"albertoalmagro\"}},{\"id\":\"8591556605\",\"type\":\"IssuesEvent\",\"actor\":{\"id\":5162312,\"login\":\"gmcgibbon\"}},{\"id\":\"8591556589\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":5162312,\"login\":\"gmcgibbon\"}},{\"id\":\"8591462698\",\"type\":\"IssuesEvent\",\"actor\":{\"id\":135265,\"login\":\"Vasfed\"}},{\"id\":\"8591394676\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8591393814\",\"type\":\"IssuesEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8591371789\",\"type\":\"PushEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8591354986\",\"type\":\"PullRequestEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8590949888\",\"type\":\"WatchEvent\",\"actor\":{\"id\":11694783,\"login\":\"realryankey\"}},{\"id\":\"8590937187\",\"type\":\"WatchEvent\",\"actor\":{\"id\":20781253,\"login\":\"robertluwang\"}},{\"id\":\"8590904108\",\"type\":\"IssuesEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8590904089\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8590899783\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":671550,\"login\":\"albertoalmagro\"}},{\"id\":\"8590834615\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":1921059,\"login\":\"Fornacula\"}},{\"id\":\"8590776702\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":94129,\"login\":\"georgeclaghorn\"}},{\"id\":\"8590668966\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8590653344\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":5162312,\"login\":\"gmcgibbon\"}},{\"id\":\"8590539379\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":3859969,\"login\":\"crantron\"}},{\"id\":\"8590474867\",\"type\":\"WatchEvent\",\"actor\":{\"id\":22120250,\"login\":\"DivinoSilva\"}},{\"id\":\"8590459050\",\"type\":\"WatchEvent\",\"actor\":{\"id\":8320256,\"login\":\"AkinoLucas\"}},{\"id\":\"8590355219\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":36071943,\"login\":\"da-development-id\"}},{\"id\":\"8590277378\",\"type\":\"WatchEvent\",\"actor\":{\"id\":3143098,\"login\":\"positr0nix\"}},{\"id\":\"8589979139\",\"type\":\"PullRequestReviewCommentEvent\",\"actor\":{\"id\":2887858,\"login\":\"deivid-rodriguez\"}},{\"id\":\"8589872468\",\"type\":\"PullRequestReviewCommentEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8589868772\",\"type\":\"ForkEvent\",\"actor\":{\"id\":10163,\"login\":\"kpumuk\"}},{\"id\":\"8589865157\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8589770759\",\"type\":\"PullRequestReviewCommentEvent\",\"actor\":{\"id\":2887858,\"login\":\"deivid-rodriguez\"}},{\"id\":\"8589745658\",\"type\":\"PullRequestReviewCommentEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8589741308\",\"type\":\"PullRequestEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8589741268\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":47848,\"login\":\"rafaelfranca\"}},{\"id\":\"8589711936\",\"type\":\"ForkEvent\",\"actor\":{\"id\":12657077,\"login\":\"abnud1\"}},{\"id\":\"8589700861\",\"type\":\"ForkEvent\",\"actor\":{\"id\":38647423,\"login\":\"istillc0de\"}},{\"id\":\"8589640032\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":2887858,\"login\":\"deivid-rodriguez\"}},{\"id\":\"8589589473\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":113026,\"login\":\"tubbo\"}},{\"id\":\"8589538099\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":28418009,\"login\":\"rails-bot[bot]\"}},{\"id\":\"8589531844\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":1796864,\"login\":\"mtsmfm\"}},{\"id\":\"8589523150\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":7468109,\"login\":\"rails-bot\"}},{\"id\":\"8589522583\",\"type\":\"PullRequestEvent\",\"actor\":{\"id\":1796864,\"login\":\"mtsmfm\"}},{\"id\":\"8589289466\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":374824,\"login\":\"hult\"}},{\"id\":\"8589285572\",\"type\":\"WatchEvent\",\"actor\":{\"id\":31253215,\"login\":\"AppleEducate\"}},{\"id\":\"8589059713\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":5926159,\"login\":\"CodingAnarchy\"}},{\"id\":\"8589013663\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":5162312,\"login\":\"gmcgibbon\"}},{\"id\":\"8588994182\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":5926159,\"login\":\"CodingAnarchy\"}},{\"id\":\"8588921161\",\"type\":\"PullRequestEvent\",\"actor\":{\"id\":5162312,\"login\":\"gmcgibbon\"}},{\"id\":\"8588893434\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":5926159,\"login\":\"CodingAnarchy\"}},{\"id\":\"8588581503\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":697113,\"login\":\"davidpiegza\"}},{\"id\":\"8588539837\",\"type\":\"WatchEvent\",\"actor\":{\"id\":10000532,\"login\":\"eleven26\"}},{\"id\":\"8588514101\",\"type\":\"IssuesEvent\",\"actor\":{\"id\":73684,\"login\":\"yahonda\"}},{\"id\":\"8588494186\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":1301152,\"login\":\"aried3r\"}},{\"id\":\"8588177319\",\"type\":\"ForkEvent\",\"actor\":{\"id\":17955768,\"login\":\"bobobrienpa\"}},{\"id\":\"8588151182\",\"type\":\"ForkEvent\",\"actor\":{\"id\":97374,\"login\":\"proton\"}},{\"id\":\"8587542913\",\"type\":\"ForkEvent\",\"actor\":{\"id\":44656531,\"login\":\"nonten\"}},{\"id\":\"8587498411\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":12156593,\"login\":\"sagg155\"}},{\"id\":\"8587443530\",\"type\":\"WatchEvent\",\"actor\":{\"id\":30570490,\"login\":\"Skupa\"}},{\"id\":\"8587363706\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":28418009,\"login\":\"rails-bot[bot]\"}},{\"id\":\"8586884567\",\"type\":\"IssuesEvent\",\"actor\":{\"id\":16337910,\"login\":\"vtm9\"}},{\"id\":\"8586836888\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":7468109,\"login\":\"rails-bot\"}},{\"id\":\"8586836508\",\"type\":\"PullRequestEvent\",\"actor\":{\"id\":2039727,\"login\":\"husam212\"}},{\"id\":\"8586811990\",\"type\":\"IssuesEvent\",\"actor\":{\"id\":374824,\"login\":\"hult\"}},{\"id\":\"8586571970\",\"type\":\"ForkEvent\",\"actor\":{\"id\":37265219,\"login\":\"alphaappsinc\"}},{\"id\":\"8586570703\",\"type\":\"WatchEvent\",\"actor\":{\"id\":2441573,\"login\":\"armanim\"}},{\"id\":\"8586527731\",\"type\":\"WatchEvent\",\"actor\":{\"id\":37256032,\"login\":\"fujimasa1031\"}},{\"id\":\"8586491437\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":1037088,\"login\":\"esparta\"}},{\"id\":\"8586468923\",\"type\":\"PushEvent\",\"actor\":{\"id\":12642,\"login\":\"kamipo\"}},{\"id\":\"8586455826\",\"type\":\"PullRequestEvent\",\"actor\":{\"id\":12642,\"login\":\"kamipo\"}},{\"id\":\"8586413278\",\"type\":\"IssueCommentEvent\",\"actor\":{\"id\":7468109,\"login\":\"rails-bot\"}},{\"id\":\"8586412309\",\"type\":\"PullRequestEvent\",\"actor\":{\"id\":1037088,\"login\":\"esparta\"}},{\"id\":\"8586218464\",\"type\":\"ForkEvent\",\"actor\":{\"id\":26560162,\"login\":\"Dpka1910\"}}]";

        ObjectMapper mapper = new ObjectMapper();

        try {
            Event[] eventArray = mapper.readValue(result, Event[].class);

            for (Event event : eventArray) {

                String login = event.getActor().getLogin();
                String type = event.getType();

                if (!resultMap.containsKey(login)) {
                    Map<String, Integer> events = new LinkedHashMap<>();
                    events.put("Total", 1);
                    events.put(type, 1);
                    resultMap.put(login, new Result(login, events));
                } else {
                    Map<String, Integer> events = resultMap.get(login).getEvents();
                    events.put("Total", events.get("Total") + 1);
                    if (!events.containsKey(type)) {
                        events.put(type, 1);
                    } else {
                        events.put(type, events.get(type) + 1);
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
//        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
//        list.sort(Map.Entry.comparingByValue());
//
//        Map<K, V> result = new LinkedHashMap<>();
//        for (Map.Entry<K, V> entry : list) {
//            result.put(entry.getKey(), entry.getValue());
//        }
//
//        return result;
//    }

//    String getStringParam(Map<String, String> param) throws UnsupportedEncodingException {
//        StringBuilder result = new StringBuilder();
//
//        int idx = 0;
//        for (String key : param.keySet()) {
//            if (idx++ != 0) {
//                result.append("&");
//            }
//            result.append(key);
//            result.append("=");
//            result.append(URLEncoder.encode(param.get(key), "UTF-8"));
//        }
//
//        return result.toString();
//    }

    String resultGetMethod(String urlString, String param) {

        BufferedReader bufferedReader = null;
        HttpURLConnection connection = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            URL url = new URL(urlString + "?" + param);
            connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String input;

                while ((input = bufferedReader.readLine()) != null) {
                    stringBuilder.append(input).append("\n");
                }
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                String input;
                while ((input = bufferedReader.readLine()) != null) {
                    stringBuilder.append(input).append("\n");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null) connection.disconnect();
        }

        return stringBuilder.toString();
    }

    String resultGetMethod(String urlString) {

        BufferedReader bufferedReader = null;
        HttpURLConnection connection = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String input;

                while ((input = bufferedReader.readLine()) != null) {
                    stringBuilder.append(input).append("\n");
                }
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                String input;
                while ((input = bufferedReader.readLine()) != null) {
                    stringBuilder.append(input).append("\n");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null) connection.disconnect();
        }

        return stringBuilder.toString();
    }

    String resultPostMethod(String urlString, String param) {

        BufferedReader bufferedReader = null;
        HttpURLConnection connection = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            URL url = new URL(urlString);
            System.out.println(urlString);
            System.out.println(param);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", "UTF-8");


            OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            writer.write(param);
            writer.flush();

            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String input;

                while ((input = bufferedReader.readLine()) != null) {
                    stringBuilder.append(input).append("\n");
                }
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                String input;
                while ((input = bufferedReader.readLine()) != null) {
                    stringBuilder.append(input).append("\n");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null) connection.disconnect();
        }

        return stringBuilder.toString();
    }

}
