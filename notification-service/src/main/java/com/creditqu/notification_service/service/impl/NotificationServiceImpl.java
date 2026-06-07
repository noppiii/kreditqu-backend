package com.creditqu.notification_service.service.impl;

import com.creditqu.common_module.constant.NotificationStatus;
import com.creditqu.common_module.constant.NotificationType;
import com.creditqu.notification_service.dto.NotificationRequestDTO;
import com.creditqu.notification_service.dto.NotificationResponseDTO;
import com.creditqu.notification_service.entity.Notification;
import com.creditqu.notification_service.repository.NotificationRepository;
import com.creditqu.notification_service.service.EmailService;
import com.creditqu.notification_service.service.NotificationService;
import com.creditqu.notification_service.service.PushNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final PushNotificationService pushService;

    @Override
    @Transactional
    public NotificationResponseDTO sendNotification(NotificationRequestDTO request) {
        log.info("Sending {} notification to user: {}", request.getType(), request.getUserId());

        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .notificationType(request.getType())
                .channel(request.getChannel())
                .title(request.getTitle())
                .content(request.getContent())
                .recipient(request.getRecipient())
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        try {
            switch (request.getType()) {
                case EMAIL:
                    emailService.sendEmail(request.getRecipient(), request.getTitle(), request.getContent());
                    break;
                case PUSH:
                    pushService.sendPushNotification(request.getRecipient(), request.getTitle(), request.getContent());
                    break;
            }

            savedNotification.setStatus(NotificationStatus.SENT);
            savedNotification.setSentAt(LocalDateTime.now());
            log.info("Notification sent successfully to: {}", request.getRecipient());

        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage());
            savedNotification.setStatus(NotificationStatus.FAILED);
            savedNotification.setErrorMessage(e.getMessage());
        }

        Notification updatedNotification = notificationRepository.save(savedNotification);
        return mapToResponseDTO(updatedNotification);
    }

    @Override
    @Transactional
    public NotificationResponseDTO sendWelcomeEmail(Long userId, String email, String name) {
        String subject = "Selamat Datang di KreditQu!";
        String content = String.format(
                "Halo %s,\n\nSelamat! Anda telah berhasil terdaftar di KreditQu. " +
                        "Sekarang Anda dapat mengajukan kartu kredit langsung dari aplikasi.\n\n" +
                        "Terima kasih telah mempercayakan kebutuhan finansial Anda kepada kami.\n\n" +
                        "Salam,\nTim KreditQu",
                name
        );

        NotificationRequestDTO request = NotificationRequestDTO.builder()
                .userId(userId)
                .recipient(email)
                .channel("WELCOME")
                .title(subject)
                .content(content)
                .type(NotificationType.EMAIL)
                .build();

        return sendNotification(request);
    }

    @Override
    @Transactional
    public NotificationResponseDTO sendOtpEmail(String email, String otp) {
        String subject = "Kode OTP KreditQu";
        String content = String.format(
                "Kode OTP Anda adalah: %s\n\n" +
                        "Kode ini berlaku selama 5 menit. Jangan berikan kode ini kepada siapapun.\n\n" +
                        "Jika Anda tidak melakukan permintaan ini, abaikan email ini.",
                otp
        );

        NotificationRequestDTO request = NotificationRequestDTO.builder()
                .userId(0L)
                .recipient(email)
                .channel("OTP")
                .title(subject)
                .content(content)
                .type(NotificationType.EMAIL)
                .build();

        return sendNotification(request);
    }

    @Override
    @Transactional
    public NotificationResponseDTO sendApplicationReceivedEmail(String email, String name, String applicationNumber) {
        String subject = "Pengajuan Kartu Kredit Diterima";
        String content = String.format(
                "Halo %s,\n\n" +
                        "Pengajuan kartu kredit Anda dengan nomor %s telah kami terima.\n" +
                        "Proses verifikasi akan memakan waktu 2x24 jam.\n\n" +
                        "Anda akan mendapatkan notifikasi setelah pengajuan diproses.\n\n" +
                        "Terima kasih,\nTim KreditQu",
                name, applicationNumber
        );

        NotificationRequestDTO request = NotificationRequestDTO.builder()
                .userId(0L)
                .recipient(email)
                .channel("APPLICATION_RECEIVED")
                .title(subject)
                .content(content)
                .type(NotificationType.EMAIL)
                .build();

        return sendNotification(request);
    }

    @Override
    @Transactional
    public NotificationResponseDTO sendApprovalEmail(String email, String name, String cardNumberMasked, String limit) {
        String subject = "Selamat! Kartu Kredit Anda Disetujui";
        String content = String.format(
                "Halo %s,\n\n" +
                        "Selamat! Pengajuan kartu kredit Anda telah DISETUJUI!\n\n" +
                        "Detail kartu:\n" +
                        "- Nomor Kartu: %s\n" +
                        "- Limit: Rp %s\n\n" +
                        "Kartu fisik akan dikirim ke alamat Anda dalam 7-14 hari kerja.\n\n" +
                        "Setelah kartu diterima, silahkan aktivasi melalui aplikasi KreditQu.\n\n" +
                        "Salam,\nTim KreditQu",
                name, cardNumberMasked, limit
        );

        NotificationRequestDTO request = NotificationRequestDTO.builder()
                .userId(0L)
                .recipient(email)
                .channel("APPROVAL")
                .title(subject)
                .content(content)
                .type(NotificationType.EMAIL)
                .build();

        return sendNotification(request);
    }

    @Override
    @Transactional
    public NotificationResponseDTO sendCardShippedEmail(String email, String name, String trackingNumber) {
        String subject = "Kartu Kredit Anda Sedang Dikirim";
        String content = String.format(
                "Halo %s,\n\n" +
                        "Kartu kredit Anda telah dikirim melalui jasa pengiriman.\n" +
                        "Nomor resi: %s\n\n" +
                        "Kartu akan tiba dalam 3-5 hari kerja.\n\n" +
                        "Setelah kartu diterima, silahkan aktivasi melalui aplikasi KreditQu.\n\n" +
                        "Salam,\nTim KreditQu",
                name, trackingNumber
        );

        NotificationRequestDTO request = NotificationRequestDTO.builder()
                .userId(0L)
                .recipient(email)
                .channel("SHIPPING")
                .title(subject)
                .content(content)
                .type(NotificationType.EMAIL)
                .build();

        return sendNotification(request);
    }

    @Override
    @Transactional
    public NotificationResponseDTO sendTransactionNotification(Long userId, String recipient, String amount, String merchant) {
        String subject = "Transaksi Kartu Kredit";
        String content = String.format(
                "Kartu kredit Anda digunakan untuk transaksi:\n" +
                        "Nominal: Rp %s\n" +
                        "Merchant: %s\n\n" +
                        "Jika transaksi ini bukan oleh Anda, segera hubungi call center kami.",
                amount, merchant
        );

        NotificationRequestDTO request = NotificationRequestDTO.builder()
                .userId(userId)
                .recipient(recipient)
                .channel("TRANSACTION")
                .title(subject)
                .content(content)
                .type(NotificationType.EMAIL)
                .build();

        return sendNotification(request);
    }

    @Override
    public List<NotificationResponseDTO> getUserNotifications(Long userId, int page, int size) {
        Page<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(
                userId, PageRequest.of(page, size)
        );
        return notifications.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.markAsRead(notificationId, LocalDateTime.now());
        log.info("Notification {} marked as read", notificationId);
    }

    private NotificationResponseDTO mapToResponseDTO(Notification notification) {
        return NotificationResponseDTO.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .channel(notification.getChannel())
                .title(notification.getTitle())
                .content(notification.getContent())
                .recipient(notification.getRecipient())
                .status(notification.getStatus().name())
                .sentAt(notification.getSentAt())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}