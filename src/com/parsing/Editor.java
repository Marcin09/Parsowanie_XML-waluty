package com.parsing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.plaf.ToolTipUI;

public class Editor extends JFrame implements ActionListener, MouseListener {
	
	JButton bCount; 
	JTextField tCurrencyInValue, tCurrencyOutValue, tDatePublication;
	JComboBox cCurrencyInName, cCurrencyOutName;
	JLabel lBackground;
	
	Parse pars = new Parse();
	
	public Editor(){
		
		setSize(850, 450);
		setTitle("Przelicznik walut");
		setLayout(null);
		setResizable(false);
		
		ImageIcon iconButton = new ImageIcon("Button.png");
		bCount = new JButton(iconButton);
		bCount.setBounds(362, 137, 125, 125);
	   	bCount.setBorderPainted(false); 
		bCount.setContentAreaFilled(false);
		add(bCount);
		bCount.addActionListener(this);
		bCount.addMouseListener(this);
		
		tDatePublication = new JTextField("Data publikacji: ");
		tDatePublication.setBounds(0, 0, 375, 30);
		tDatePublication.setFont(new Font("SansSerif", Font.ITALIC, 14));
		tDatePublication.setEditable(false);
		tDatePublication.setText("Ostatnia data publikacji kursów przez NBP:  " 
								+ pars.currencyData("date").get(2).toString());
		add(tDatePublication);
		
		tCurrencyInValue = new JTextField("");
		tCurrencyInValue.setBounds(40, 180, 100, 30);
		tCurrencyInValue.setFont(new Font("SansSerif", Font.CENTER_BASELINE, 15));
		ToolTipManager.sharedInstance().setDismissDelay(10000);
		ToolTipManager.sharedInstance().setInitialDelay(0);
		tCurrencyInValue.setToolTipText("Podaj kwotê");
		tCurrencyInValue.setDocument(new JTextFieldLimit(7));
		add(tCurrencyInValue);
		tCurrencyInValue.setActionCommand(iconButton.getDescription());
		tCurrencyInValue.addActionListener(this);
		tCurrencyInValue.addMouseListener(this);
		
		tCurrencyOutValue = new JTextField("Wynik");
		tCurrencyOutValue.setBounds(540, 180, 100, 30);
		tCurrencyOutValue.setFont(new Font("SansSerif", Font.CENTER_BASELINE, 15));
		tCurrencyOutValue.setEditable(false);
		add(tCurrencyOutValue);	
		
		cCurrencyInName = new JComboBox(pars.currencyData("name").toArray());
		cCurrencyInName.setBounds(160, 180, 170, 30);
		cCurrencyInName.setFont(new Font("SansSerif", Font.PLAIN, 14));
		add(cCurrencyInName);
		cCurrencyInName.addActionListener(this);
		
		cCurrencyOutName = new JComboBox(pars.currencyData("name").toArray());
		cCurrencyOutName.setBounds(660, 180, 170, 30);
		cCurrencyOutName.setFont(new Font("SansSerif", Font.PLAIN, 14));
		add(cCurrencyOutName);
		cCurrencyOutName.addActionListener(this);
		
		ImageIcon iconBackground = new ImageIcon("Background.jpg");
		lBackground = new JLabel("Image and Text", iconBackground, JLabel.CENTER);
		lBackground.setBounds(0, 0, 850, 450);
		add(lBackground);	
	}
	public static void main(String[] args) {
	
		SwingUtilities.invokeLater(new Runnable() {
			
		@Override
		public void run() {
        	Editor okno = new Editor();
        	okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	okno.setVisible(true);
        	okno.setLocationRelativeTo(null);
			}
		});
	}
	@Override
	public void actionPerformed(ActionEvent e) {
	
		Object source = e.getSource();
		
		if (source == bCount || source == tCurrencyInValue){
			
		ArrayList list = new ArrayList<>(pars.currencyData("name"));
		String currencyNameIn = cCurrencyInName.getSelectedItem().toString();
		double courseValueIn = 0;
		double converterValueIn = 0;
		
		for (int i = 0; i<list.size(); i++){
			if (currencyNameIn.equals(list.get(i)) && currencyNameIn.equals("z³oty polski")) {
			courseValueIn = 1.0;
			converterValueIn = 1.0;
			}
			else if (currencyNameIn.equals(list.get(i)) && !currencyNameIn.equals("Wybierz walutê...")){
			try{
				if (pars.currencyData("course").get(i).contains(","))
				courseValueIn = (double) NumberFormat.getInstance().parse(pars.currencyData("course").get(i));
				else courseValueIn = Double.parseDouble(pars.currencyData("course").get(i));
				converterValueIn = Double.parseDouble(pars.currencyData("converter").get(i));
			}catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
	}	
		String currencyNameOut = cCurrencyOutName.getSelectedItem().toString();
		double courseValueOut = 0;
		double converterValueOut = 0;
		
		for (int i = 0; i<list.size(); i++){
			if (currencyNameOut.equals(list.get(i)) && currencyNameOut.equals("z³oty polski")) {
			courseValueOut = 1.0;
			converterValueOut = 1.0;
			}
			else if (currencyNameOut.equals(list.get(i))  && !currencyNameOut.equals("Wybierz walutê...")){
			try{
				if (pars.currencyData("course").get(i).contains(","))
				courseValueOut = (double) NumberFormat.getInstance().parse(pars.currencyData("course").get(i));
				else courseValueOut = Double.parseDouble(pars.currencyData("course").get(i));
				converterValueOut = Double.parseDouble(pars.currencyData("converter").get(i));
			}catch (ParseException e2) {
				e2.printStackTrace();
			}
		}
	}	
	try{
		
		String valueIn = tCurrencyInValue.getText();
		double numberIn;
	
		if (valueIn.contains(","))
		numberIn = (double) NumberFormat.getInstance().parse(valueIn);
		else numberIn = Double.parseDouble(valueIn);
	
		double result = numberIn * courseValueIn * converterValueOut / (courseValueOut * converterValueIn);
		result *= 100;
		result = Math.round(result);
		result /= 100;
		String valueOut = String.valueOf(result);
		tCurrencyOutValue.setText(valueOut);
	}catch (NumberFormatException | NullPointerException | ClassCastException | ParseException e3){
		
		tCurrencyInValue.setDocument(new JTextFieldLimit(12));
		tCurrencyInValue.setText("Podaj cyfry!");
		tCurrencyInValue.setForeground(Color.RED);
		tCurrencyOutValue.setForeground(Color.RED);
		tCurrencyOutValue.setText("Podaj cyfry!");
			} 	 	
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	
	Object source = e.getSource();
	if (source == bCount){
	bCount.setBorderPainted(true);
	}
	if (source == tCurrencyInValue){
		if (tCurrencyInValue.getText().equals("Podaj cyfry!")){
			tCurrencyInValue.setDocument(new JTextFieldLimit(7));
			tCurrencyInValue.setText("");
			tCurrencyOutValue.setText("");
			tCurrencyInValue.setForeground(Color.BLACK);
			tCurrencyOutValue.setForeground(Color.BLACK);
			}
		}
	}
	@Override
	public void mouseExited(MouseEvent e) {

	Object source = e.getSource();
	if (source == bCount)
	bCount.setBorderPainted(false);	
	}

	@Override
	public void mousePressed(MouseEvent e) {
	
	Object source = e.getSource();
	
	if (source == tCurrencyInValue){
		if (tCurrencyInValue.getText().equals("Podaj cyfry!")){
			tCurrencyInValue.setDocument(new JTextFieldLimit(7));
			tCurrencyInValue.setText("");
			tCurrencyOutValue.setText("");
			tCurrencyInValue.setForeground(Color.BLACK);
			tCurrencyOutValue.setForeground(Color.BLACK);
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {}
}

