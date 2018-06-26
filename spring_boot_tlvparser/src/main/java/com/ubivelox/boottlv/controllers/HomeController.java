package com.ubivelox.boottlv.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ubivelox.gaia.GaiaException;
import com.ubivelox.gaia.util.GaiaUtils;

import exception.UbiveloxException;
import tlvparser.TLVObject;
import tlvparser.TLVParser;
import tlvparser.TLVParserWithArrayList;

@Controller
public class HomeController
{

    @RequestMapping(value = "/json/string/{hexaString}",
                    method = RequestMethod.GET,
                    produces = "text/plain; charset=utf-8")
    @ResponseBody
    public String getTlvParserString(final HttpServletRequest request, @PathVariable final String hexaString) throws UbiveloxException, GaiaException
    {
        GaiaUtils.checkHexaString(hexaString);
        // readline이 라인별로 받기 때문에 이렇게 치환
        return TLVParser.parse(hexaString)
                        .replaceAll("\n", "♨")
                        .replaceAll("\t", "♬");
    }





    @RequestMapping(value = "/json/list/{hexaString}",
                    method = RequestMethod.GET,
                    produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getTlvParserList(final HttpServletRequest request, @PathVariable final String hexaString, final ModelMap modelMap) throws UbiveloxException, GaiaException
    {
        GaiaUtils.checkNull(request, modelMap);

        GaiaUtils.checkHexaString(hexaString);

        byte[] byteArray = GaiaUtils.convertHexaStringToByteArray(hexaString);

        List<TLVObject> tlvList = TLVParserWithArrayList.parse1(hexaString, byteArray, 0, -1);

        List<Map<String, Object>> treeList = convertArrayListToMap(tlvList);

        JSONObject jObject = new JSONObject();
        jObject.put("list", treeList);

        return jObject.toString();
    }





    private List<Map<String, Object>> convertArrayListToMap(final List<TLVObject> tlvList) throws GaiaException
    {
        TLVObject tlvObject = null;
        String tag = "";
        String length = "";
        String stringValue = "";

        GaiaUtils.checkNull(tlvList);

        List<Map<String, Object>> treeList = new ArrayList<>();
        for ( int i = 0; i < tlvList.size(); i++ )
        {
            Map<String, Object> map = new HashMap<>();
            tlvObject = tlvList.get(i);
            tag = tlvObject.getTag();
            length = tlvObject.getLength();

            map.put("tag", tag);
            map.put("length", length);

            // construct가 있다는 것
            if ( tlvObject.getValue() != null )
            {
                map.put("value", convertArrayListToMap(tlvObject.getValue()));
            }
            else
            {
                stringValue = tlvObject.getStringValue();
                map.put("value", stringValue);
            }

            treeList.add(map);
        }
        return treeList;
    }





    @RequestMapping(method = { RequestMethod.POST, RequestMethod.GET },
                    value = "/convert")
    public String input(final HttpServletRequest request, final ModelMap modelMap) throws UbiveloxException, GaiaException
    {
        return "input_view";
    }





    @RequestMapping(method = { RequestMethod.POST, RequestMethod.GET },
                    value = "/convert.do")
    public String input2(final HttpServletRequest request, final ModelMap modelMap) throws UbiveloxException, GaiaException
    {

        GaiaUtils.checkNull(request, modelMap);

        GaiaUtils.checkHexaString(request.getParameter("HexaString"));

        String hexString = request.getParameter("HexaString");
        byte[] byteArray = GaiaUtils.convertHexaStringToByteArray(hexString);

        ArrayList<TLVObject> tlvList = TLVParserWithArrayList.parse1(hexString, byteArray, 0, -1);

        modelMap.addAttribute("TlvList", tlvList);
        modelMap.addAttribute("HexaString", request.getParameter("HexaString"));

        return "input_view2";
    }
}
