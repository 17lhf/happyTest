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

    private static final String FILE_STORE_FOLDER = "static/fileIO/emptyFolder/";

    // todo 单文件上传

    /**
     * 多文件上传 <br/>
     * 此删除的@RequestParam中的value值务必要和前端FormData的key值保持一致（前端对应代码请看前端项目）<br/>
     * @param files 前端上传上来的文件数据
     * @return 成功的情况下的应答
     */
    @PostMapping("files")
    public Result<String> uploadFiles(@RequestParam("files")MultipartFile[] files) throws IOException {
        // 本次上传的文件暂放的文件夹
        String folderPath = FileIO.getAbsolutePath(FILE_STORE_FOLDER) + "/" + System.currentTimeMillis();
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
