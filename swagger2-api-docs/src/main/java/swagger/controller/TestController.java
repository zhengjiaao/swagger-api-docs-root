///**
// * @Company: 上海***技术有限公司
// * @Department: 数据中心
// * @Author: 宇宙小神特别萌
// * @Email: ***
// * @Date: 2021-12-17 18:42
// * @Since:
// */
//package swagger.controller;
//
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//public class TestController {
//
//    @ApiOperation( value="批量保存日志")
//    @PostMapping(value = "public/v1/log/batch")
//    public Object SaveLog(@ApiParam(value = "日志参数说明请查看接口public/v1/log/parameter")@RequestBody List<Map<String,String>> logParams) throws Exception {
//        return "111";
//    }
//
//    @ApiOperation( value="批量保存日志")
//    @PostMapping(value = "public/v1")
//    public Object v_1(@ApiParam(value = "日志参数说明请查看接口public/v1/log/parameter")@RequestBody Map<String,String> logParams) throws Exception {
//        return "111";
//    }
//
//    @ApiOperation( value="批量保存日志")
//    @PostMapping(value = "public/v2")
//    public Object v_2(@ApiParam(value = "日志参数说明请查看接口public/v1/log/parameter")@RequestBody List<String> logParams) throws Exception {
//        return "111";
//    }
//
//    @ApiOperation( value="批量保存日志")
//    @PostMapping(value = "public/v3")
//    public Object v_3(@ApiParam(value = "日志参数说明请查看接口public/v1/log/parameter")@RequestBody List<Integer> logParams) throws Exception {
//        return "111";
//    }
//}
