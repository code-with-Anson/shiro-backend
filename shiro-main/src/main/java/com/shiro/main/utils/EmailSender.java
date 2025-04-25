package com.shiro.main.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
@Component
@Slf4j
public class EmailSender {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;


    public void sendVerifyCode(String toEmail, String verifyCode) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(String.format("汐落 <%s>", fromEmail));
            helper.setTo(toEmail);
            helper.setSubject("登录验证码");

            String htmlContent = String.format("""
                    <div style="background-color: #f7f7f7; padding: 20px;">
                        <div style="max-width: 600px; margin: 0 auto; background-color: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                            <p style="color: #666; font-size: 16px; line-height: 1.5;">Hello！</p>
                            <p style="color: #666; font-size: 16px; line-height: 1.5;">这是你的登录验证码，请在5分钟内完成验证</p>
                            <div style="background-color: #f5f5f5; padding: 15px; margin: 16px 0; border-radius: 4px; text-align: center;">
                                <span style="font-size: 24px; color: #333; font-weight: bold; letter-spacing: 4px;">%s</span>
                            </div>
                            <p style="color: #666; font-size: 14px; line-height: 1.5;">请不要泄露哦</p>
                        </div>
                    </div>
                    """, verifyCode);

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            log.info("验证码已发送至 {}", toEmail);
        } catch (MessagingException | MailException e) {
            log.error("发送验证码至 {} 失败", toEmail, e);
            throw new RuntimeException("邮件发送失败", e);
        }
    }


    /**
     * 发送Excel附件到用户邮箱
     *
     * @param toEmail   收件人邮箱
     * @param subject   邮件主题
     * @param excelData Excel数据字节数组
     * @param fileName  附件文件名
     */
    public void sendExcelAttachment(String toEmail, String subject, byte[] excelData, String fileName) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(String.format("汐落 <%s>", fromEmail));
            helper.setTo(toEmail);
            helper.setSubject(subject);

            // 构建邮件正文
            String htmlContent = String.format("""
                    <div style="background-color: #f7f7f7; padding: 20px;">
                        <div style="max-width: 600px; margin: 0 auto; background-color: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                            <h2 style="color: #333;">您的账单数据</h2>
                            <p style="color: #666; font-size: 16px; line-height: 1.5;">您好！</p>
                            <p style="color: #666; font-size: 16px; line-height: 1.5;">您请求的账单数据已生成，请查看附件。</p>
                            <p style="color: #666; font-size: 14px; margin-top: 20px;">此邮件为系统自动发送，请勿回复。</p>
                        </div>
                    </div>
                    """);

            helper.setText(htmlContent, true);

            // 添加Excel附件
            ByteArrayResource resource = new ByteArrayResource(excelData);
            helper.addAttachment(fileName, resource);

            mailSender.send(mimeMessage);
            log.info("账单Excel已发送至 {}", toEmail);
        } catch (MessagingException | MailException e) {
            log.error("发送Excel附件至 {} 失败", toEmail, e);
            throw new RuntimeException("邮件发送失败", e);
        }
    }
}
