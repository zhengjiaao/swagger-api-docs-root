package swagger.swagger2word;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;

@Api(tags = "Swagger 转成 Word文档 - [Html模板] - 支持所有swagger版本")
@Slf4j
@Controller
@RequestMapping("/html")
public class SwaggerToWordController {

    @Autowired
    private WordService tableService;

    //默认解析器，无法读取jar包中的模板资源
//    @Autowired
//    private SpringTemplateEngine springTemplateEngine;

    private String fileName = "swagger-api";

    /**
     * 本地缓存
     */
    @Value("${localCache}")
    private String localCache;

    /**
     * 上传的自定义模板存储
     */
    private String localTemplatesPath;

    @PostConstruct
    public void init() {
        localTemplatesPath = localCache + "/templates/" + "html" + "/" + "word.html";
    }

    @ApiOperation(value = "下载模板示例", notes = "下载[*.html]模板，编辑后可使用自定义模板")
    @GetMapping("/download/word/template")
    public void toWordByUrl(HttpServletResponse response) throws Exception {
        Locale currentLocale = Locale.getDefault();
        String localizedTemplate = "word-" + currentLocale.getLanguage() + "_" + currentLocale.getCountry();
        String filePath = "/templates/html/" + localizedTemplate + ".html";

        String templatesName = "swagger-api.html";

        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(templatesName.getBytes("UTF-8"), "ISO-8859-1") + "\".docx;filename*=UTF-8''" + new String(templatesName.getBytes("UTF-8"), "ISO-8859-1"));

        InputStream resourceAsStream = this.getClass().getResourceAsStream(filePath);
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(resourceAsStream, outputStream);
        outputStream.close();
    }

    @ApiOperation(value = "默认模板-在线 swagger api URL", notes = "转换之后下载doc文档")
    @PostMapping("/to/word/by_url")
    public void word(HttpServletResponse response,
                     Model model,
                     @ApiParam(value = "swagger api-docs url", defaultValue = "https://petstore.swagger.io/v2/swagger.json", required = true) @RequestParam String url) {
        generateModelData(model, url, 0);
        writeContentToResponse(false, model, response);
    }

    @ApiOperation(value = "默认模板-离线 swagger api jsonFile", notes = "转换之后下载doc文档")
    @PostMapping("/to/word/by_jsonfile")
    public void toWordByJsonFile(HttpServletResponse response,
                                 Model model,
                                 @ApiParam(value = "上传swagger api json文件", required = true) @Valid @RequestPart MultipartFile file) throws IOException {
        generateModelData(model, file);
        writeContentToResponse(false, model, response);
    }

    @ApiOperation(value = "默认模板-离线 swagger api jsonStr", notes = "转换之后下载doc文档")
    @PostMapping("/to/word/by_jsonstr")
    public void toWordByJsonStr(HttpServletResponse response,
                                Model model,
                                @ApiParam(value = "上传swagger api json 字符串", required = true) @Valid @RequestParam("jsonStr") String jsonStr) throws IOException {
        generateModelData(model, jsonStr);
        writeContentToResponse(false, model, response);
    }

    //jar 中会找不到模板
    /*@ApiOperation(value = "默认模板-在线 swagger api URL-[在线预览]", notes = "转换之后下载doc文档")
    @GetMapping("/to/word/by_url/v2")
    public String getWord(Model model,
                          @ApiParam(value = "swagger api-docs url", defaultValue = "https://petstore.swagger.io/v2/swagger.json", required = true) @RequestParam String url,
                          @ApiParam("显示 下载按钮 默 true") @RequestParam(required = false, defaultValue = "true") boolean download) {
        generateModelData(model, url, true == download ? 1 : 0);

        Locale currentLocale = Locale.getDefault();
        String localizedTemplate = "/html/word-" + currentLocale.getLanguage() + "_" + currentLocale.getCountry();
        String fileName = "/templates" + localizedTemplate + ".html";

        if (getClass().getResourceAsStream(fileName) != null) {
            log.info(fileName + " resource found");
            return localizedTemplate;
        } else {
            log.info(fileName + " resource not found, using default");
        }
        return "templates/html/word";
    }*/

    @ApiOperation(value = "自定义模板[*.html]-在线 swagger api URL", notes = "转换之后下载doc文档")
    @PostMapping("/my/to/word/by_url")
    public void word2(HttpServletResponse response,
                      Model model,
                      @ApiParam(value = "上传模板文件 *.html", required = true) @Valid @RequestPart MultipartFile tfile,
                      @ApiParam(value = "swagger api-docs url", defaultValue = "https://petstore.swagger.io/v2/swagger.json", required = true) @RequestParam String url) throws IOException {
        //自定义模板文件
        File templateFile = createFile(localTemplatesPath);
        tfile.transferTo(templateFile);

        generateModelData(model, url, 0);
        writeContentToResponse(true, model, response);
    }

    @ApiOperation(value = "自定义模板[*.html]-离线 swagger api jsonFile", notes = "转换之后下载doc文档")
    @PostMapping("/my/to/word/by_jsonfile")
    public void toWordByJsonFile(HttpServletResponse response,
                                 Model model,
                                 @ApiParam(value = "上传模板文件 *.html", required = true) @Valid @RequestPart MultipartFile tfile,
                                 @ApiParam(value = "上传swagger api json文件", required = true) @Valid @RequestPart MultipartFile file) throws IOException {
        //自定义模板文件
        File templateFile = createFile(localTemplatesPath);
        tfile.transferTo(templateFile);

        generateModelData(model, file);
        writeContentToResponse(true, model, response);
    }

    @ApiOperation(value = "自定义模板[*.html]-离线 swagger api jsonStr", notes = "转换之后下载doc文档")
    @PostMapping("/my/to/word/by_jsonstr")
    public void toWordByJsonStr(HttpServletResponse response,
                                Model model,
                                @ApiParam(value = "上传模板文件 *.html", required = true) @Valid @RequestPart MultipartFile tfile,
                                @ApiParam(value = "上传swagger api json 字符串", required = true) @Valid @RequestParam("jsonStr") String jsonStr) throws IOException {
        //自定义模板文件
        File templateFile = createFile(localTemplatesPath);
        tfile.transferTo(templateFile);

        generateModelData(model, jsonStr);
        writeContentToResponse(true, model, response);
    }

    @ApiOperation(value = "在线预览Html 一键下载为[*.doc]文档", hidden = true, notes = "swagger 不显示此接口")
    @GetMapping("/downloadWord")
    public void downloadWord(HttpServletResponse response,
                             Model model,
                             @ApiParam("swagger api-docs url") @RequestParam String url) {
        generateModelData(model, url, 0);
        writeContentToResponse(true, model, response);
    }


    //------------------私有方法------------------

    private void generateModelData(Model model, String jsonStr) {
        Map<String, Object> result = tableService.tableListFromString(jsonStr);
        model.addAttribute("url", "http://");
        model.addAttribute("download", 0);
        model.addAllAttributes(result);
    }

    private void generateModelData(Model model, MultipartFile jsonFile) {
        Map<String, Object> result = tableService.tableList(jsonFile);
        fileName = jsonFile.getOriginalFilename();

        if (fileName != null) {
            fileName = fileName.replaceAll(".json", "");
        } else {
            fileName = "swagger-api-word";
        }

        model.addAttribute("url", "http://");
        model.addAttribute("download", 0);
        model.addAllAttributes(result);
    }

    private void generateModelData(Model model, String url, Integer download) {
        Map<String, Object> result = tableService.tableList(url);
        model.addAttribute("url", url);
        model.addAttribute("download", download);
        model.addAllAttributes(result);
    }

    private void writeContentToResponse(boolean customTemplate, Model model, HttpServletResponse response) {
        Context context = new Context();
        context.setVariables(model.asMap());
        String content = "";
        if (customTemplate) {
            content = createLocalSpringTemplateEngine().process("/html/word", context);
        } else {
            content = createClassLoaderSpringTemplate().process("/html/word", context);
            //无法读取jar包中模板
            //content = springTemplateEngine.process("/html/word", context);
        }
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".doc", "utf-8"));
            byte[] bytes = content.getBytes();
            bos.write(bytes, 0, bytes.length);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File createFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 自定义Spring模板引擎
     * 目的：解决读取本地绝对路径的模板
     */
    private SpringTemplateEngine createLocalSpringTemplateEngine() {
        FileTemplateResolver templateResolver = new FileTemplateResolver();
        templateResolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
        templateResolver.setCacheable(false);
        templateResolver.setPrefix(localCache + "/templates/");
        templateResolver.setSuffix(".html");

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    /**
     * 自定义Spring模板引擎
     * 目的：解决打成jar包后，无法读取使用jar中的模板
     */
    private SpringTemplateEngine createClassLoaderSpringTemplate() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }


}
