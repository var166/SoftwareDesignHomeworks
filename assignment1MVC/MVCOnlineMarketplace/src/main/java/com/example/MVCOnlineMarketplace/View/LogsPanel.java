package com.example.MVCOnlineMarketplace.View;

import com.example.MVCOnlineMarketplace.Controller.NotificationController;
import com.example.MVCOnlineMarketplace.Documents.EmailLogEntry;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Component
public class LogsPanel extends JPanel {

    private final NotificationController notificationController;

    private JTable logTable;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;

    public LogsPanel(NotificationController notificationController) {
        this.notificationController = notificationController;
        setLayout(new BorderLayout());

        String[] columns = {"To", "Subject", "Status", "Error", "Sent At"};
        this.tableModel = new DefaultTableModel(columns, 0);

        List<EmailLogEntry> logs = notificationController.getEmailLogs();
        for (EmailLogEntry log : logs) {
            tableModel.addRow(new Object[]{
                    log.getTo(),
                    log.getSubject(),
                    log.getStatus(),
                    log.getErrorMessage(),
                    log.getSentAt()
            });
        }

        this.logTable = new JTable(tableModel);
        this.scrollPane = new JScrollPane(logTable);
        add(scrollPane, BorderLayout.CENTER);
    }
}
