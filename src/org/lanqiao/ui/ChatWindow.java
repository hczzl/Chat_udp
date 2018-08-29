package org.lanqiao.ui;

import javax.swing.*;

import org.lanqiao.adapter.MyKeyAdapter;
import org.lanqiao.adapter.SendFileActionListener;
import org.lanqiao.adapter.SendMessagePersonActionLister;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatWindow {
	JFrame f;
	public JTextArea ta;
	public JTextField tf;
	@SuppressWarnings("rawtypes")
	public JComboBox cb;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ChatWindow(String username) {
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 400);
		f.setTitle(username);
		f.setLocation(300, 200);

		ta = new JTextArea();
		JScrollPane sp = new JScrollPane(ta);
		ta.setEditable(false);
		cb = new JComboBox();
		cb.addItem("All");
		cb.addActionListener(
				new  ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						String name=(String)cb.getSelectedItem();
						if(!name.equals("All")) {
							PersonChatWindow pw = new PersonChatWindow(username);
						}
						
					}
				}
				);
		tf = new JTextField();
		tf.addKeyListener(new MyKeyAdapter(this,username));

		JButton jb = new JButton("私聊窗口");
	
		JButton jbSendFile = new JButton("传文件");//生成一个传文件的按钮，并且对这个按钮实行监听事件
		jbSendFile.addActionListener(new SendFileActionListener(cb));
		JPanel pl = new JPanel(new BorderLayout());
		pl.add(cb);
		pl.add(jb, BorderLayout.WEST);
		JPanel p = new JPanel(new BorderLayout());
		p.add(pl, BorderLayout.WEST);
		p.add(tf);
		p.add(jbSendFile,BorderLayout.EAST);
		f.getContentPane().add(p, BorderLayout.SOUTH);
		f.getContentPane().add(sp);
		f.setVisible(true);// 窗口可见
	}
}