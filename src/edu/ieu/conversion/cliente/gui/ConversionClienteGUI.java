package edu.ieu.conversion.cliente.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTextPane;

import edu.ieu.conversion.cliente.socket.ConversionCliente;

import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.awt.event.ActionEvent;

public class ConversionClienteGUI {

	private JFrame frame;
	private JButton btnConvertir;
	private JComboBox comboBox;
	private JComboBox comboBox_1;
	private JTextField textField;
	private JTextPane textPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConversionClienteGUI window = new ConversionClienteGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ConversionClienteGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel_central = new JPanel();
		frame.getContentPane().add(panel_central, BorderLayout.CENTER);
		panel_central.setLayout(new GridLayout(3, 5, 5, 5));
		
		JLabel lblNewLabel = new JLabel("Moneda origen");
		panel_central.add(lblNewLabel);
		
		comboBox = new JComboBox();
		comboBox.addItem("USD");
		comboBox.addItem("EUR");
		comboBox.addItem("HKD");
		panel_central.add(comboBox);
		
		JLabel lblNewLabel_1 = new JLabel("Moneda Destino");		
		panel_central.add(lblNewLabel_1);
		
		comboBox_1 = new JComboBox();
		comboBox_1.addItem("USD");
		comboBox_1.addItem("EUR");
		comboBox_1.addItem("HKD");
		panel_central.add(comboBox_1);
		
		JLabel lblNewLabel_2 = new JLabel("Cantidad");
		panel_central.add(lblNewLabel_2);
		
		textField = new JTextField();
		panel_central.add(textField);
		textField.setColumns(10);
		
		JPanel panel_derecha = new JPanel();
		frame.getContentPane().add(panel_derecha, BorderLayout.EAST);
		FlowLayout fl_panel_derecha = new FlowLayout(FlowLayout.CENTER, 5, 5);
		panel_derecha.setLayout(fl_panel_derecha);
		
		btnConvertir = new JButton("Convertir");
		btnConvertir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String monedaOrigen = (String) comboBox.getSelectedItem();
				String monedaDestino = (String) comboBox_1.getSelectedItem();
				String cantidad = textField.getText();
				enviarDatosServidor(monedaOrigen, monedaDestino, cantidad);				
			}
		});
		panel_derecha.add(btnConvertir);
		
		JButton btSalir = new JButton("Salir");
		panel_derecha.add(btSalir);
		
		textPane = new JTextPane();
		frame.getContentPane().add(textPane, BorderLayout.SOUTH);
	}

	public void enviarDatosServidor(String origen,String destino,String cantidad) {
		try {
			double cantidadDouble = Double.parseDouble(cantidad);
			FutureTask<String> task = 
					new FutureTask (
							new ConversionCliente(origen, destino, cantidadDouble)
					);
			ExecutorService es = Executors.newSingleThreadExecutor ();
			es.submit(task);
			while(true) {
				if(task.isDone()) {
					String valor= task.get();						
					textPane.setText( valor );
					break;
				}
			}
		}catch(Exception ex) {
			mostrarDialogoError(ex.getMessage());
			ex.printStackTrace();
			return;
		}		
	}
	
	public void mostrarDialogoError(String msg) {
		 // create a dialog Box
        JDialog d = new JDialog(frame, "Error");
        // create a label
        JLabel l = new JLabel("Error: " + msg);
        d.add(l);
        // setsize of dialog
        d.setSize(100, 100);
        // set visibility of dialog
        d.setVisible(true);
	}
}
