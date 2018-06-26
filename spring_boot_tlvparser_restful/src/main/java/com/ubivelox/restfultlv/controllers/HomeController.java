package com.ubivelox.restfultlv.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ubivelox.gaia.GaiaException;
import com.ubivelox.gaia.util.GaiaUtils;

import exception.UbiveloxException;
import tlvparser.TLVObject;
import tlvparser.TLVParser;
import tlvparser.TLVParserWithArrayList;

@Controller
public class HomeController
{

    @RequestMapping(value = "/tlvparser/json/string",
                    method = RequestMethod.GET,
                    produces = "text/plain; charset=utf-8")
    public String getTlvParserString(final HttpServletRequest request, @RequestParam final String hexaString, final ModelMap modelMap) throws UbiveloxException, GaiaException
    {
        GaiaUtils.checkNull(modelMap);
        GaiaUtils.checkHexaString(hexaString);

        boolean listCheck = false; // list넘김인지 String 넘김인지 flag

        StringBuffer result = getParser("http://localhost:8080/json/string/" + hexaString);

        String resultString = result.toString();

        modelMap.addAttribute("listCheck", listCheck);
        modelMap.addAttribute("HexaString", hexaString);
        modelMap.addAttribute("Result", resultString);

        return "input_view";
    }





    @RequestMapping(value = "/tlvparser/json/list",
                    method = RequestMethod.GET,
                    produces = "text/plain; charset=utf-8")
    public String getTlvParserList(@RequestParam final String hexaString, final ModelMap modelMap) throws UbiveloxException, GaiaException, ParseException
    {
        GaiaUtils.checkNull(modelMap);
        GaiaUtils.checkHexaString(hexaString);
        boolean listCheck = true; // list넘김인지 String 넘김인지 flag

        StringBuffer result = getParser("http://localhost:8080/json/list/" + hexaString);

        // 여기서 처리

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(result.toString());
        JSONObject jsonObj = (JSONObject) obj; // 최상위

        JSONArray jArray = (JSONArray) jsonObj.get("list");

        ArrayList<TLVObject> tlvList = convertJsonToArrayList(new ArrayList<TLVObject>(), jArray);

        modelMap.addAttribute("listCheck", listCheck);
        modelMap.addAttribute("TlvList", tlvList);
        modelMap.addAttribute("HexaString", hexaString);

        return "input_view";
    }





    private ArrayList<TLVObject> convertJsonToArrayList(final ArrayList<TLVObject> tlvList, final JSONArray jArray) throws GaiaException
    {
        String tag = "";
        String length = "";
        String value = "";

        GaiaUtils.checkNull(tlvList, jArray);

        // for문 한 번 돌 때가 최 상위
        for ( int i = 0; i < jArray.size(); i++ )
        {
            JSONObject jsonObj = (JSONObject) jArray.get(i);

            tag = (String) jsonObj.get("tag");
            length = (String) jsonObj.get("length");

            // constructed
            if ( jsonObj.get("value") instanceof JSONArray )
            {
                JSONArray jValueArray = (JSONArray) jsonObj.get("value");
                tlvList.add(new TLVObject(tag, length, convertJsonToArrayList(new ArrayList<TLVObject>(), jValueArray)));

                // primitived
            }
            else
            {

                value = (String) jsonObj.get("stringValue");

                tlvList.add(new TLVObject(tag, length, value));
            }

        }

        return tlvList;
    }





    @RequestMapping(method = { RequestMethod.POST, RequestMethod.GET },
                    value = "/convert")
    public String input(final HttpServletRequest request, final ModelMap modelMap) throws UbiveloxException, GaiaException
    {
        return "input_view";
    }





    private StringBuffer getParser(final String urlString) throws GaiaException, UbiveloxException
    {
        BufferedReader in = null;

        StringBuffer result = new StringBuffer();

        GaiaUtils.checkNull(urlString);

        try
        {
            URL obj = new URL(urlString); // 호출할 url
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

            String line;
            while ( (line = in.readLine()) != null )
            { // response를 차례대로 출력
                result.append(line);
                result.append("\n");
            }
        }
        catch ( Exception e )
        {
            throw new UbiveloxException("URL 통신 에러");
        }
        finally
        {
            if ( in != null )
            {
                try
                {
                    in.close();
                }
                catch ( Exception e )
                {
                    throw new UbiveloxException("BufferedReader 에러");
                }
            }
        }

        return result;

    }





    @RequestMapping(value = "/tlvparser/jar/string",
                    method = RequestMethod.GET)
    public String getTlvParserString2(final HttpServletRequest request, @RequestParam final String hexaString, final ModelMap modelMap) throws UbiveloxException, GaiaException
    {

        boolean listCheck = false; // list넘김인지 String 넘김인지 flag

        GaiaUtils.checkHexaString(hexaString);
        modelMap.addAttribute("listCheck", listCheck);
        modelMap.addAttribute("HexaString", hexaString);
        modelMap.addAttribute("Result", TLVParser.parse(hexaString));

        return "input_view";

    }





    @RequestMapping(value = "/tlvparser/jar/list",
                    method = RequestMethod.GET)
    public String input2(final HttpServletRequest request, final ModelMap modelMap, @RequestParam final String hexaString) throws UbiveloxException, GaiaException
    {
        GaiaUtils.checkHexaString(hexaString);

        boolean listCheck = true; // list넘김인지 String 넘김인지 flag
        String hexString = hexaString;

        byte[] byteArray = GaiaUtils.convertHexaStringToByteArray(hexString);

        ArrayList<TLVObject> tlvList = TLVParserWithArrayList.parse1(hexString, byteArray, 0, -1);

        modelMap.addAttribute("listCheck", listCheck);
        modelMap.addAttribute("TlvList", tlvList);
        modelMap.addAttribute("HexaString", hexaString);

        return "input_view";
    }

}
