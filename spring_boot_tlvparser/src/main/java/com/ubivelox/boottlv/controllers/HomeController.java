package com.ubivelox.boottlv.controllers;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ubivelox.gaia.GaiaException;
import com.ubivelox.gaia.util.GaiaUtils;

import exception.UbiveloxException;
import tlvparser.TLVObject;
import tlvparser.TLVParserWithArrayList;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController
{

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);





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
