/**
 * @Company: 上海***技术有限公司
 * @Department: 数据中心
 * @Author: 宇宙小神特别萌
 * @Email: ***
 * @Date: 2021-12-21 14:23
 * @Since:
 */
package swagger.poitl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 使用poi-tl模板
 * 来源：https://github.com/Sayi/poi-tl
 * 更新说明：进行二次开发、优化、修复部分问题，自定义模板
 */
@Api(tags = {"Swagger2 转成 Word文档 - [Word模板] - 仅支持swagger2版本"})
@RestController
@RequestMapping("/poitl")
public class Swagger2ToWordByPotTlController {

    @Autowired
    Swagger2ToWordByPoiTl swagger2ToWordByPoiTl;

    /**
     * 本地缓存
     */
    @Value("${localCache}")
    private String localCache;

    /**
     * 本地缓存路径
     */
    private String localPath;

    /**
     * 上传的自定义模板存储
     */
    private String localTemplatesPath;

    @PostConstruct
    public void init() {
        localPath = localCache + File.separator + "word" + File.separator;
        localTemplatesPath = localCache + "/templates/" + "word" + "/" + "swagger-api.docx";
    }

    @ApiOperation(value = "下载模板示例", notes = "下载[*.docx]模板，编辑后可使用自定义模板")
    @GetMapping("/download/word/template")
    public void toWordByUrl(HttpServletResponse response,
                            @ApiParam(value = "可选 1 or 2", defaultValue = "1", required = true) @RequestParam int template) throws Exception {

        String resourcePath = resourcePath(template);

        String fileName = "swagger-api-word-" + template + ".docx";
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\".docx;filename*=UTF-8''" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));

        InputStream resourceAsStream = this.getClass().getResourceAsStream(resourcePath);
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(resourceAsStream, outputStream);
        outputStream.close();
    }

    @ApiOperation(value = "默认模板-在线 swagger2 api URL", notes = "转换之后下载docx文档")
    @PostMapping("/to/word/by_url")
    public void toWordByUrl(HttpServletResponse response,
                            @ApiParam(value = "可选 1 or 2", defaultValue = "1", required = true) @RequestParam int template,
                            @ApiParam(value = "swagger2 api-docs url", defaultValue = "https://petstore.swagger.io/v2/swagger.json", required = true) @RequestParam String url) throws Exception {

        String resourcePath = resourcePath(template);

        String fileName = "swagger-api-word-" + template + ".docx";
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\".docx;filename*=UTF-8''" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));

        swagger2ToWordByPoiTl.toWord(url, resourcePath, null, response);
    }

    @ApiOperation(value = "默认模板-离线 swagger2 api jsonFile", notes = "转换之后下载docx文档")
    @PostMapping("/to/word/by_jsonfile")
    public void toWordByJsonFile(HttpServletResponse response,
                                 @ApiParam(value = "可选 1 or 2", defaultValue = "1", required = true) @RequestParam int template,
                                 @ApiParam(value = "上传swagger api json文件", required = true) @Valid @RequestPart MultipartFile file) throws IOException {
        String resourcePath = resourcePath(template);

        File jsonfile = new File("D:\\swagger2-jsonfile.json");
        if (jsonfile.exists()) {
            jsonfile.delete();
        }
        file.transferTo(jsonfile);
        String absolutePath = jsonfile.getAbsolutePath();

        String fileName = "swagger-api-word-" + template + ".docx";
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\".docx;filename*=UTF-8''" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));

        swagger2ToWordByPoiTl.toWord(absolutePath, resourcePath, null, response);
    }

    @ApiOperation(value = "默认模板-离线 swagger2 api jsonStr", notes = "转换之后下载docx文档")
    @PostMapping("/to/word/by_jsonstr")
    public void toWordByJsonStr(HttpServletResponse response,
                                @ApiParam(value = "可选 1 or 2", defaultValue = "1", required = true) @RequestParam int template,
                                @ApiParam(value = "上传swagger api json 字符串", required = true) @Valid @RequestParam("jsonStr") String jsonStr) throws IOException {
        String resourcePath = resourcePath(template);

        String jsonfile = localPath + "swagger2-jsonfile.json";
        boolean isSuccess = createJsonFile(jsonStr, jsonfile);
        if (isSuccess) {
            String fileName = "swagger-api-word-" + template + ".docx";
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\".docx;filename*=UTF-8''" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));

            swagger2ToWordByPoiTl.toWord(jsonfile, resourcePath, null, response);
        }
    }

    @ApiOperation(value = "自定义模板[*.docx]-在线 swagger2 api URL", notes = "转换之后下载docx文档")
    @PostMapping("my/to/word/by_url")
    public void toWordByUrl(HttpServletResponse response,
                            @ApiParam(value = "上传模板文件 *.docx", required = true) @Valid @RequestPart MultipartFile tfile,
                            @ApiParam(value = "swagger2 api-docs url", defaultValue = "https://petstore.swagger.io/v2/swagger.json", required = true) @RequestParam String url) throws Exception {

        //自定义模板文件
        File templateFile = createFile(localTemplatesPath);
        tfile.transferTo(templateFile);

        String fileName = "swagger-api-word-my.docx";
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\".docx;filename*=UTF-8''" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));

        swagger2ToWordByPoiTl.toWord(url, null, templateFile, response);
    }

    @ApiOperation(value = "自定义模板[*.docx]-离线 swagger2 api jsonFile", notes = "转换之后下载docx文档")
    @PostMapping("my/to/word/by_jsonfile")
    public void toWordByJsonFile(HttpServletResponse response,
                                 @ApiParam(value = "上传模板文件 *.docx", required = true) @Valid @RequestPart MultipartFile tfile,
                                 @ApiParam(value = "上传swagger api json文件", required = true) @Valid @RequestPart MultipartFile file) throws IOException {

        //自定义模板文件
        File templateFile = createFile(localTemplatesPath);
        tfile.transferTo(templateFile);

        //swagger api jsonfile
        File jsonfile = createFile(localPath + "swagger2-jsonfile.json");
        file.transferTo(jsonfile);
        String jsonFilePath = jsonfile.getAbsolutePath();

        String fileName = "swagger-api-word-my.docx";
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\".docx;filename*=UTF-8''" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));

        swagger2ToWordByPoiTl.toWord(jsonFilePath, null, templateFile, response);
    }

    @ApiOperation(value = "自定义模板[*.docx]-离线 swagger2 api jsonStr", notes = "转换之后下载docx文档")
    @PostMapping("my/to/word/by_jsonstr")
    public void toWordByJsonStr(HttpServletResponse response,
                                @ApiParam(value = "上传模板文件 *.docx", required = true) @Valid @RequestPart MultipartFile tfile,
                                @ApiParam(value = "上传swagger api json 字符串", required = true) @Valid @RequestParam("jsonStr") String jsonStr) throws IOException {
        //自定义模板文件
        File templateFile = createFile(localTemplatesPath);
        tfile.transferTo(templateFile);

        String jsonfile = localPath + "swagger2-jsonfile.json";
        boolean isSuccess = createJsonFile(jsonStr, "D:\\swagger2-jsonfile.json");
        if (isSuccess) {
            String fileName = "swagger-api-word-my.docx";
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\".docx;filename*=UTF-8''" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));

            swagger2ToWordByPoiTl.toWord(jsonfile, null, templateFile, response);
        }
    }


    private String resourcePath(int template) {
        if (template == 1) {
            return "/templates/word/swagger-1.docx";
        } else if (template == 2) {
            return "/templates/word/swagger-2.docx";
        } else {
            throw new RuntimeException("参数错误：template = 1 or 2");
        }

    }

    public static boolean createJsonFile(String jsonData, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            Writer write = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            write.write(jsonData);
            write.flush();
            write.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

}
