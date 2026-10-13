package com.example.MVCOnlineMarketplace;

import com.example.MVCOnlineMarketplace.Controller.ProductController;
import com.example.MVCOnlineMarketplace.Dto.ProductDto;
import com.example.MVCOnlineMarketplace.Model.*;
import com.example.MVCOnlineMarketplace.Repositories.OrderRepository;
import com.example.MVCOnlineMarketplace.Repositories.ProductRepository;
import com.example.MVCOnlineMarketplace.Repositories.ShopRepository;
import com.example.MVCOnlineMarketplace.Repositories.UserRepository;
import com.example.MVCOnlineMarketplace.Service.ProductService;
import com.example.MVCOnlineMarketplace.Service.ProductServiceImplementation;
import com.example.MVCOnlineMarketplace.View.MainFrame;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class MvcOnlineMarketplaceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = new SpringApplicationBuilder(MvcOnlineMarketplaceApplication.class)
				.headless(false)
				.run(args);

		SwingUtilities.invokeLater(() -> {
			MainFrame mainFrame = context.getBean(MainFrame.class);
			mainFrame.setVisible(true);
		});

	}


}
