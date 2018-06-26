package com.ubivelox.boottlv.controllers;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
        return TLVParser.parse(hexaString);
    }





    @RequestMapping(value = "/json/list/{hexaString}",
                    method = RequestMethod.GET,
                    produces = "application/json; charset=utf-8")
    @ResponseBody
    public HashMap<String, Object> getTlvParserList(final HttpServletRequest request, @PathVariable final String hexaString, final ModelMap modelMap) throws UbiveloxException, GaiaException
    {
        GaiaUtils.checkNull(request, modelMap);

        GaiaUtils.checkHexaString(hexaString);

        byte[] byteArray = GaiaUtils.convertHexaStringToByteArray(hexaString);

        List<TLVObject> tlvList = TLVParserWithArrayList.parse1(hexaString, byteArray, 0, -1);

        HashMap<String, Object> treeList = new HashMap<>();

        treeList.put("list", tlvList);

        return treeList;
    }

    // private List<Map<String, Object>> convertArrayListToMap(final List<TLVObject> tlvList) throws GaiaException
    // {
    // TLVObject tlvObject = null;
    // String tag = "";
    // String length = "";
    // String stringValue = "";
    //
    // GaiaUtils.checkNull(tlvList);
    //
    // List<Map<String, Object>> treeList = new ArrayList<>();
    // for ( int i = 0; i < tlvList.size(); i++ )
    // {
    // Map<String, Object> map = new HashMap<>();
    // tlvObject = tlvList.get(i);
    // tag = tlvObject.getTag();
    // length = tlvObject.getLength();
    //
    // map.put("tag", tag);
    // map.put("length", length);
    //
    // // construct가 있다는 것
    // if ( tlvObject.getValue() != null )
    // {
    // map.put("value", convertArrayListToMap(tlvObject.getValue()));
    // }
    // else
    // {
    // stringValue = tlvObject.getStringValue();
    // map.put("value", stringValue);
    // }
    //
    // treeList.add(map);
    // }
    // return treeList;
    // }

}
