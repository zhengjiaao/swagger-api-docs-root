package poi.example;

import com.deepoove.poi.data.BookmarkTextRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.plugin.highlight.HighlightRenderData;
import io.swagger.models.ExternalDocs;
import io.swagger.models.Info;
import io.swagger.models.Scheme;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SwaggerView {

    protected String swagger = "2.0";
    protected Info info;
    protected String host;
    protected String time;
    protected String basePath;
    protected List<Scheme> schemes;
    protected ExternalDocs externalDocs;
    protected List<Resource> resources;
    protected List<Definition> definitions;

    public String getSwagger() {
        return swagger;
    }

    public void setSwagger(String swagger) {
        this.swagger = swagger;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public List<Scheme> getSchemes() {
        return schemes;
    }

    public void setSchemes(List<Scheme> schemes) {
        this.schemes = schemes;
    }

    public ExternalDocs getExternalDocs() {
        return externalDocs;
    }

    public void setExternalDocs(ExternalDocs externalDocs) {
        this.externalDocs = externalDocs;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

}

class Resource {
    private String name;
    private String description;
    private List<Endpoint> endpoints;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

}

class Endpoint {
    private List<String> tag;
    private String summary;
    private String hostIp = "";
    private String httpMethod;
    private boolean isGet;
    private boolean isPut;
    private boolean isPost;
    private boolean isDelete;
    private String url;
    private String requestURL;
    private String formData;
    private String header;
    private String description;
    private boolean deprecated;
    private List<Parameter> parameters;
    private List<Response> responses;
    private String response;
    private List<String> produces;
    private String firstProduce;
    private List<String> consumes;
    private String firstConsume;

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public boolean isGet() {
        return isGet;
    }

    public void setGet(boolean isGet) {
        this.isGet = isGet;
    }

    public boolean isPut() {
        return isPut;
    }

    public void setPut(boolean isPut) {
        this.isPut = isPut;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean isPost) {
        this.isPost = isPost;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getFirstProduce() {
        List<String> produces = getProduces();
        if (ObjectUtils.isNotEmpty(produces)) {
            firstProduce = produces.get(0);
        }
        if (!StringUtils.isEmpty(firstProduce)) {
            firstProduce = "\\ \n -H 'accept: " + firstProduce + "'";
        }
        return firstProduce;
    }

    public String getFirstConsume() {
        List<String> consumes = getConsumes();
        if (ObjectUtils.isNotEmpty(consumes)) {
            firstConsume = consumes.get(0);
        }
        if (!StringUtils.isEmpty(firstConsume)) {
            firstConsume = "\\ \n -H 'Content-Type: " + firstConsume + "'";
        }
        return firstConsume;
    }

    public String getRequestURL() {

        List<Parameter> parameters = this.parameters;
        if (ObjectUtils.isEmpty(parameters)) {
            return hostIp + url;
        }

        String requestURL = hostIp + url;

        String query = "";
        String formData = "";
        String header = "";

        if (isGet || isDelete) {

            for (Parameter p : parameters) {
                //路径上的参数不处理
                /*if (p.getIn().equals("path")) {
                }*/
                if (p.getIn().equals("query")) {
                    String dataType = "";
                    String schema = p.getSchema().toString();
                    if (schema.contains(",")) {
                        dataType = schema.replaceAll(",", "").replaceAll(" ", "");
                    } else {
                        dataType = schema;
                    }

                    if (StringUtils.isEmpty(query)) {
                        query = p.getName() + "=" + dataType;
                    } else {
                        query = query + "&" + p.getName() + "=" + dataType;
                    }
                }

                if (p.getIn().equals("formData")) {

                }

                if (p.getIn().equals("header")) {
                    if (StringUtils.isEmpty(header)) {
                        header = "\\ \n -H '" + p.getName() + "=" + p.getSchema();
                    } else {
                        header = header + "\\ \n -H '" + p.getName() + "=" + p.getSchema();
                    }
                }
            }

            if (!StringUtils.isBlank(header)) {
                this.header = header;
            }

            if (!StringUtils.isBlank(query)) {
                requestURL = requestURL + "?" + query;
            }

            return requestURL;
        }

        if (isPost || isPut) {
            for (Parameter p : parameters) {
                //路径上的参数不处理
                /*if (p.getIn().equals("path")) {
                }*/
                if (p.getIn().equals("formData")) {
                    String dataType = "";
                    String schema = p.getSchema().toString();
                    if (schema.contains(",")) {
                        dataType = schema.replaceAll(",", "");
                    } else {
                        dataType = schema;
                    }

                    if (StringUtils.isEmpty(formData)) {
                        formData = "\\ \n -F '" + p.getName() + "=" + dataType + "'";
                    } else {
                        formData = formData + "\\ \n -F '" + p.getName() + "=" + dataType + "'";
                    }
                }

                /**
                 * {@link Parameter.body}
                 */
                /*if (p.getIn().equals("body")) {
                }*/

                if (p.getIn().equals("header")) {
                    if (StringUtils.isEmpty(header)) {
                        header = "\\ \n -H '" + p.getName() + "=" + p.getSchema();
                    } else {
                        header = header + "\\ \n -H '" + p.getName() + "=" + p.getSchema();
                    }
                }
            }

            if (!StringUtils.isBlank(header)) {
                this.header = header;
            }

            if (!StringUtils.isBlank(formData)) {
                this.formData = formData;
            }

            return requestURL;
        }
        return requestURL;
    }

    public String getFormData() {
        return formData;
    }

    public String getHeader() {
        return header;
    }
}

class Parameter {
    private String in;
    private String name;
    private boolean required;
    private String description;
    private List<TextRenderData> schema;
    private String defaultValue;
    private String body;

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TextRenderData> getSchema() {
        return schema;
    }

    public void setSchema(List<TextRenderData> schema) {
        this.schema = schema;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getBody() {
        if (StringUtils.isEmpty(body)) {
            return body;
        }

        body = "\\ \n -d '" + body + "'";
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

class Response {
    private String code;
    private String description;
    private List<TextRenderData> schema;
    private List<Header> headers;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TextRenderData> getSchema() {
        return schema;
    }

    public void setSchema(List<TextRenderData> schema) {
        this.schema = schema;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

}

class Header {
    private String name;
    private String type;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

class Definition {
    private BookmarkTextRenderData name;
    private String description;
    List<Property> properties;
    HighlightRenderData definitionCode;

    public BookmarkTextRenderData getName() {
        return name;
    }

    public void setName(BookmarkTextRenderData name) {
        this.name = name;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public HighlightRenderData getDefinitionCode() {
        return definitionCode;
    }

    public void setDefinitionCode(HighlightRenderData definitionCode) {
        this.definitionCode = definitionCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

class Property {
    private String name;
    private boolean required;
    private List<TextRenderData> schema;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<TextRenderData> getSchema() {
        return schema;
    }

    public void setSchema(List<TextRenderData> schema) {
        this.schema = schema;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
