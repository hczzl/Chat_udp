package org.lanqiao.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.lanqiao.adapter.MyActionListener;
import org.lanqiao.adapter.MyMouseAdapter;

public class LoginWindow
{
	public static void main(String args[])
	{
		JFrame frame=new JFrame("��¼����");//�������
		frame.setSize(400,300);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel accountJLabel=new JLabel("�ʺţ�");
		accountJLabel.setBounds(70,100,40,20);

		JLabel passwordJLabel=new JLabel("���룺");
		passwordJLabel.setBounds(70,120,40,20);//40���ؿ�

		JTextField accountJTF=new JTextField("liwei");
		accountJTF.addMouseListener(new MyMouseAdapter(accountJTF));
		accountJTF.setBounds(120,100,180,20);

		JTextField passwordJTF=new JTextField("lw1234");
		passwordJTF.addMouseListener(new MyMouseAdapter(passwordJTF));
		passwordJTF.setBounds(120,120,180,20);

		JButton loginButton=new JButton("��¼");
		loginButton.addActionListener(new MyActionListener(accountJTF,passwordJTF,frame));
		loginButton.setBounds(120,180,180,30);
		frame.getContentPane().setLayout(null);//���������ΰڷ�
		
		frame.getContentPane().add(accountJLabel);//�õ����������Ұ������ӵ�������
		frame.getContentPane().add(passwordJLabel);
		frame.getContentPane().add(accountJTF);
		frame.getContentPane().add(passwordJTF);
		frame.getContentPane().add(loginButton);
	
		frame.setVisible(true);//�����ʾ����
	}
}