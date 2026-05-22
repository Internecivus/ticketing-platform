package com.faust.ticketing.core.service.messaging.template;

import com.faust.ticketing.core.exception.SystemException;
import com.faust.ticketing.core.configuration.ServerConfiguration;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.ext.beans.ResourceBundleModel;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public final class TemplateService {
    private final static Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
    private final static String TEMPLATES_DIRECTORY_NAME = "email";
    private final static String TEMPLATE_EXTENSION = ".ftl";
    public final static BeansWrapper beansWrapper = new BeansWrapperBuilder(Configuration.VERSION_2_3_21).build();

    static {
        try {
            configuration.setDefaultEncoding(ServerConfiguration.CHARSET_NAME);
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            configuration.setLogTemplateExceptions(false);
            configuration.setWrapUncheckedExceptions(true);
            configuration.setFallbackOnNullLoopVariable(false);
            configuration.setDirectoryForTemplateLoading(new File(TemplateService.class.getClassLoader().getResource(TEMPLATES_DIRECTORY_NAME).getPath()));
            configuration.setObjectWrapper(beansWrapper);
        }
        catch (final IOException e) {
            throw new SystemException(e);
        }
    }

    public static String process(final Template name, final Object data) {
        try {
            final freemarker.template.Template template = configuration.getTemplate(name.getName() + TEMPLATE_EXTENSION);
            final StringWriter stringWriter = new StringWriter();
            template.process(data, stringWriter);
            return stringWriter.toString();
        }
        catch (final IOException | TemplateException e) {
            throw new SystemException(e);
        }
    }
}
