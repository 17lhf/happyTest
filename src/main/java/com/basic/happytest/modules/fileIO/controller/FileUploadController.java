package com.basic.happytest.modules.fileIO.controller;

import com.basic.happytest.modules.fileIO.FileIO;
import com.basic.happytest.modules.uniformPackagingReturn.resp.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传控制层
 * @author lhf
 */

@RestController
@RequestMapping("/upload-file/")
@CrossOrigin
public class FileUploadController {

    private static final String FILE_STORE_FOLDER = "/static/fileIO/uploadTmp/";

    /**
     * 处理上传的文件（结合postman测试）
     * @param file 上传的文件数据
     * @return 结束标识
     */
    @PostMapping("dealWithFile")
    public Result<String> dealWithFile(@RequestParam("file")MultipartFile file) {
        if (file == null) {
            System.out.println("上传内容为空");
            return Result.success("");
        }
        // 这里获取到的是，文件原始的文件名
        System.out.println("上传文件的原始文件名：" + file.getOriginalFilename());
        // 这里获取到的是，上传时设置的报文的字段的名字
        System.out.println("上传文件的文件名：" + file.getName());
        // 如果是通过前端上传，则往往会自动依据文件后缀进行设置这个值，导致这里获取的话其实并不是特别准确
        // 实际获取文件类型，是处理文件内容，获取前n位，然后与一个对应表进行比较，哪个匹配就说明它是哪个文件类型
        // 但是，实测发现，部分文件是没有文件头的，所以判断起来可能存在问题
        System.out.println("上传文件的类型：" + file.getContentType());
        // 单位：Byte
        System.out.println("上传文件的大小：" + file.getSize());
        // todo 获取文件头，文件头与常见标准文件头匹配以识别真实的文件类型
        return Result.success("");
    }

    /**
     * 多文件上传 <br/>
     * 此删除的@RequestParam中的value值务必要和前端FormData的key值保持一致（前端对应代码请看前端项目）<br/>
     * @param files 前端上传上来的文件数据
     * @return 成功的情况下的应答
     */
    @PostMapping("files")
    public Result<String> uploadFiles(@RequestParam("files")MultipartFile[] files) throws IOException {
        // 本次上传的文件暂放的文件夹
        String folderPath = FileIO.getAbsolutePath(FILE_STORE_FOLDER) + System.currentTimeMillis();
        FileIO.createFolder(folderPath);
        // 开始往文件夹中存放上传上来的文件
        for (MultipartFile multipartFile : files) {
            // 就暂定以上传方原始的文件名作为我们暂存的文件名
            String filePath = folderPath + "/" + multipartFile.getOriginalFilename();
            File file = new File(filePath);
            if(!file.createNewFile()){
                System.out.println("文件已存在，请确认是否同名：" + filePath);
                continue;
            }
            multipartFile.transferTo(file);
        }
        return Result.success("");
    }
}
