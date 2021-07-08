package com.example.demo.Configs;

import com.example.demo.Params.ResponseError;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.IOException;

@Component
public class ErrorHandler extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write(convertObjectToJson(cleanError(e)));
        }
    }

    private ResponseError cleanError(Exception e) {

        var originalException = e.getCause();

        var res = ResponseError.builder()
                .errors(null)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("Internal Server Error")
                .build();

        if (originalException instanceof HttpClientErrorException) {
            res.setStatus(((HttpClientErrorException) originalException).getStatusCode());
            res.setMessage(((HttpClientErrorException) originalException).getStatusText());
        }

        return res;
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {

        if (object == null) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        String error = mapper.writeValueAsString(object);

        System.out.println(error);

        return error;
    }

}