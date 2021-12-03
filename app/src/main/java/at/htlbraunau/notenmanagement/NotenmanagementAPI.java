package at.htlbraunau.notenmanagement;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NotenmanagementAPI {

    private static final String TAG = "NotenmanagementClient";

    private static final String BASE_URL = "https://notenmanagement.htl-braunau.at/rest/";

    private static String accessToken;
    private static String matrikelNr;

    public static boolean login(String username, String password){

        HttpURLConnection huc = null;
        BufferedReader br = null;

        try {
            URL url = new URL(BASE_URL + "token");

            huc = (HttpURLConnection) url.openConnection();

            huc.setRequestMethod("POST");
            huc.setDoOutput(true);

            huc.setRequestProperty("Content-type","application/x-www-form-urlencoded");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(huc.getOutputStream()));
            bw.write("grant_type=password&username=" + username + "&password=" + URLEncoder.encode(password,"UTF-8"));
            bw.flush();
            bw.close();

            int responseCode = huc.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK)   {
                br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
                String response = br.readLine();

                JSONObject jsonLoginResult = new JSONObject(response);
                accessToken = jsonLoginResult.getString("access_token");
                matrikelNr = jsonLoginResult.getString("matrikelNr");

                return true;
            } else {
                Log.e(TAG,responseCode+"");

                return false;
            }

        } catch (Exception ex)  {
            Log.e(TAG,ex.getMessage());

            return false;
        } finally {
            if(br != null)  {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(huc != null) {
                huc.disconnect();
            }
        }
    }

    public static String[] getSubjects() {
        HttpURLConnection huc = null;
        BufferedReader br = null;
        String[] subjects = null;

        try {
            URL url = new URL(BASE_URL + "api/Schueler/" + matrikelNr + "/Faecher");

            huc = (HttpURLConnection) url.openConnection();

            huc.setRequestProperty("Authorization", "bearer " + accessToken);

            int responseCode = huc.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK)   {
                br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
                String response = br.readLine();

                JSONArray jsonSubjects = new JSONArray(response);
                
                subjects = new String[jsonSubjects.length()];
                
                for(int i = 0; i < jsonSubjects.length();i++)   {
                    JSONObject jsonSubject = jsonSubjects.getJSONObject(i);
                    subjects[i] = jsonSubject.getString("Fach");
                }
                

            } else {
                Log.e(TAG,responseCode+"");


            }

        } catch (Exception ex)  {
            Log.e(TAG,ex.getMessage());
        } finally {
            if(br != null)  {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(huc != null) {
                huc.disconnect();
            }

            return subjects;
        }
    }

    public static LFResult[] getLFs(String subject) {
        HttpURLConnection huc = null;
        BufferedReader br = null;
        LFResult[] lfResults = null;

        try {
            URL url = new URL(BASE_URL + "api/Schueler/" + matrikelNr + "/Faecher/" + subject +"/Noten");

            huc = (HttpURLConnection) url.openConnection();

            huc.setRequestProperty("Authorization", "bearer " + accessToken);

            int responseCode = huc.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK)   {
                br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
                String response = br.readLine();

                Log.i(TAG,response);

                JSONArray jsonLFResults = new JSONArray(response);

            } else {
                Log.e(TAG,responseCode+"");


            }

        } catch (Exception ex)  {
            Log.e(TAG,ex.getMessage());
        } finally {
            if(br != null)  {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(huc != null) {
                huc.disconnect();
            }

            return lfResults;
        }

    }
}
