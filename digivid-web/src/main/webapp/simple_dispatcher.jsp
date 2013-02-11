<%@ page import="java.io.UnsupportedEncodingException"%>
<%@ page import="dk.statsbiblioteket.deck.client.webinterface.WebConstants" %>
<%
    response.setCharacterEncoding("UTF-8");
    String character_encoding = request.getCharacterEncoding();
    if (character_encoding == null || !character_encoding.equalsIgnoreCase("UTF-8")) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            //No idea what to do here
        }
    }
    request.setAttribute(WebConstants.PAGE_ATTR, request.getParameter(WebConstants.PAGE_PARAM));
    session.getServletContext().getRequestDispatcher(WebConstants.INDEX_JSP).forward(request, response);
%>
