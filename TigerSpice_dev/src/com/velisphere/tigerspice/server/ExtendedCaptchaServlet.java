package com.velisphere.tigerspice.server;

import static nl.captcha.Captcha.NAME;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.gimpy.FishEyeGimpyRenderer;
import nl.captcha.noise.StraightLineNoiseProducer;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.servlet.SimpleCaptchaServlet;

@SuppressWarnings("serial")
public class ExtendedCaptchaServlet extends SimpleCaptchaServlet {
    
    
     
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        
        Captcha captcha = new Captcha.Builder(250, 75)
        .addText()
        .addBackground(new GradiatedBackgroundProducer())
        .gimp(new FishEyeGimpyRenderer())
        .addNoise(new StraightLineNoiseProducer())
        .addBorder()
        .build();
        

        session.setAttribute(NAME, captcha);
        CaptchaServletUtil.writeImage(resp, captcha.getImage());
        
    }
}