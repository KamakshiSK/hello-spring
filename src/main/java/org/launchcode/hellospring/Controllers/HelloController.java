package org.launchcode.hellospring.Controllers;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HelloController {
    static HashMap<String, String> helloTranslate=new HashMap<>();

    public static void setLanguage()
    {
        helloTranslate.put("English", "Hello");
        helloTranslate.put("Spanish", "Hola");
        helloTranslate.put("French", "Bonjour");
        helloTranslate.put("Chinese", "你好(Nǐ hǎo)");
        helloTranslate.put("Hindi", "नमस्ते");
    }
/*    // localhost:8080/
    @RequestMapping(value="")
    @ResponseBody
    public String index(){
        return "Hello World";
    }*/

    // localhost:8080/goodbye
    @RequestMapping(value="goodbye")
    @ResponseBody
    public String goodbye(){
        return "Good Bye";
    }

    // localhost:8080?name=Foo  #Query parameter #GET request
    @RequestMapping(value = "")
    @ResponseBody
    public String index(HttpServletRequest request)
    {
        String name = request.getParameter("name");
        if (name == null)
            name = "World!";
        return "Hello " + name;
    }

    // localhost:8080?name=Foo  #Query parameter #GET request
    @RequestMapping(value = "hello", method = RequestMethod.GET)
    @ResponseBody
    public String helloForm(){
        setLanguage();
        String html_str="";

        for(Map.Entry<String, String> lang: helloTranslate.entrySet())
            html_str += "<option value='" + lang.getKey() + "'>" + lang.getKey() + "</option>";

        String html = "<form method='post'>" +
                      "<label>Enter your name here : </label>" +
                      "<input type='text' name='name' />" +
                      "<br><br>" +
                      "<label for = 'language_sel'>Select language</label>" +
                      "<select id='language_sel' name = 'language'>" +
                      "<option value='English'>--Please select a language</option>" + html_str +
                      "</select>" +
                      "<br><br>" +
                      "<input type='submit' value='Greet Me!'/>" +
                      "</form>";
        return html;
    }

    // upon clicking on Greet me button
    @RequestMapping(value = "hello", method = RequestMethod.POST)
    @ResponseBody
    public String helloPost(HttpServletResponse response, HttpServletRequest request){
        String userName = request.getParameter("name");
        String language = request.getParameter("language");

        HttpSession session = request.getSession();

        Cookie[] requestCookies = request.getCookies();
        Cookie greetCookie = new Cookie(userName, "1");
        int cookieValue;

        if(requestCookies!= null) {
            for (Cookie c : requestCookies) {
                if (c.getName().equals(userName)) {
                    cookieValue = Integer.valueOf(c.getValue());
                    greetCookie.setValue(Integer.toString(++cookieValue));
                    break;
                }
            }
        }

        response.addCookie(greetCookie);
        System.out.println(requestCookies);

        String greetings = "<html>" +
                "<h1 style= 'color:coral'><b><i>" + createMessage(userName, language) + "</b></i></h1>" +
                           "<h6><i>Greeted : " + greetCookie.getValue()  + " times.</i></h6>" +
                           "</html>";
        return greetings;
    }

    public static String createMessage(String name, String language)
    {
        String returnLanguage;
        returnLanguage = helloTranslate.get(language);
        return returnLanguage + " " + name;
    }

    // localhost:8080/hello/foo #URL Segment
    @RequestMapping(value="hello/{name}{last}")
    @ResponseBody
    public String helloURLSegment(@PathVariable String name, @PathVariable String last)
    {
        return "Hello " + name + " last: " + last;
    }
}

