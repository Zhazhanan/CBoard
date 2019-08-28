package org.cboard.cboardservice.modules.services;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.cboard.cboardservice.modules.pojo.DashboardJob;
import org.cboard.cboardservice.modules.services.persist.PersistContext;
import org.cboard.cboardservice.modules.services.persist.excel.XlsProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by yfyuan on 2017/2/10.
 *
 * @author WangKun
 * @date 2019/04/12
 */
@Service
public class MailService {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private XlsProcessService xlsProcessService;

    @Autowired
    private PersistService persistService;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String mail_from;

    public static final String ATTACHMENT_NAME = "report.xls";

    private Function<Object, PersistContext> getPersistBoard(List<PersistContext> persistContextList) {
        return e -> persistContextList.stream()
                .filter(board -> {
                    Long boardId = board.getDashboardId();
                    return boardId != null && boardId.equals((((JSONObject) e).getLong("id")));
                })
                .findFirst().get();
    }

    public void sendDashboard(DashboardJob job) throws Exception {
        JSONObject config = JSONObject.parseObject(job.getConfig());

        List<PersistContext> persistContextList = config.getJSONArray("boards").stream()
                .map(e -> persistService.persist(((JSONObject) e).getLong("id"), job.getUserId()))
                .collect(Collectors.toList());

        List<PersistContext> workbookList = config.getJSONArray("boards").stream()
                .filter(e -> ((JSONObject) e).getString("type").contains("xls"))
                .map(getPersistBoard(persistContextList))
                .collect(Collectors.toList());

        ByteArrayOutputStream baos = null;
        if (workbookList != null && workbookList.size() > 0) {
            HSSFWorkbook workbook = xlsProcessService.dashboardToXls(workbookList);
            if (StringUtils.isNotEmpty(config.getString("xlspwd"))) {
                Biff8EncryptionKey.setCurrentUserPassword(config.getString("xlspwd"));
            }
            baos = new ByteArrayOutputStream();
            try {
                workbook.write(baos);
            } catch (IOException e) {
                LOG.error("", e);
            }
        }

        List<PersistContext> picList = config.getJSONArray("boards").stream()
                .filter(e -> ((JSONObject) e).getString("type").contains("img"))
                .map(getPersistBoard(persistContextList))
                .collect(Collectors.toList());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

        helper.setFrom(mail_from);
        helper.setSubject(config.getString("subject"));

        String to = config.getString("to");
        helper.setTo(to.split(";"));

        String cc = config.getString("cc");
        helper.setCc(cc.split(";"));

        String bcc = config.getString("bcc");
        helper.setBcc(bcc.split(";"));

        if (baos != null) {
            ByteArrayDataSource ds = new ByteArrayDataSource(baos.toByteArray(), ContentType.APPLICATION_OCTET_STREAM.getMimeType());
            helper.addAttachment(ATTACHMENT_NAME, ds);
        }

        StringBuilder sb = new StringBuilder("<html><body>");
        picList.forEach(v -> sb.append("<img src='cid:").append(v.getDashboardId()).append("'/></br>"));
        helper.setText(sb.toString(), true);

        picList.forEach(v -> {
            String b64 = v.getData().getString("img");
            byte[] bytes = Base64.getDecoder().decode(b64.substring(23));
            ByteArrayDataSource ds = new ByteArrayDataSource(bytes, ContentType.APPLICATION_OCTET_STREAM.getMimeType());
            try {
                helper.addInline(String.valueOf(v.getDashboardId()), ds);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        mailSender.send(message);
    }
}
