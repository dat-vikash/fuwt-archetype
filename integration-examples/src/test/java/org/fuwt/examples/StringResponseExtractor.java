package org.fuwt.examples;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.HttpMessageConverterExtractor;

import java.io.*;
import java.util.List;

/**
 * User: chris
 * Date: 5/13/11
 * Time: 2:15 AM
 */
public class StringResponseExtractor extends HttpMessageConverterExtractor<String>
{
    /**
     * Creates a new instance of the {@code HttpMessageConverterExtractor} with the given response type and message
     * converters. The given converters must support the response type.
     */
    public StringResponseExtractor(Class<String> responseType, List<HttpMessageConverter<?>> messageConverters)
    {
        super(responseType, messageConverters);
    }


    @Override
    public String extractData(final ClientHttpResponse response) throws IOException
    {
        BufferedReader reader=new BufferedReader(new InputStreamReader(response.getBody()));
        StringBuffer responseAsString=new StringBuffer();
        String line;
        while ((line=reader.readLine())!=null)
            responseAsString.append(line).append('\n');

        return responseAsString.toString();

    }
}
