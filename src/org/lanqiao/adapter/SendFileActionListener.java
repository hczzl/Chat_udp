package org.lanqiao.adapter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import org.lanqiao.util.DBConnection;

public class SendFileActionListener implements ActionListener {

	JComboBox cb;

	public SendFileActionListener(JComboBox cb) {
		this.cb = cb;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * JFileChooser类：为用户选择文件提供了一种简单的机制
		 */
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(null);// 弹出一个 "Open File" 文件选择器对话框,返回一个整型数据，设置为null是什么意思
		if (returnVal == JFileChooser.APPROVE_OPTION) {// 如果点了确定；选择确认（yes、ok）后返回该值。
			String fileName = chooser.getSelectedFile().getAbsolutePath();// 首先是 返回选中的文件。然后 返回此抽象路径名的绝对路径名字符串。
			//文件的路径
			Socket socket = null;
			FileInputStream fis = null;
			/**FileInputStream
			 *  从文件系统中的某个文件中获得输入字节
			 *  用于读取诸如图像数据之类的原始字节流。要读取字符流，请考虑使用 FileReader。
			 */
			try {

				Connection conn = null;
				conn = DBConnection.getConn();
				String sql = "SELECT ip,tcpport FROM tb_online_user WHERE username=?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, ((String) cb.getSelectedItem()).trim());// 先获取JComboBox中选中要给谁发送文件，然后清楚前后的空格，再强转为String
				ResultSet rs = pstmt.executeQuery();// 执行查询
				rs.next();
				String ip = rs.getString("IP");
				int tcpPort = rs.getInt("TCPPORT");

				// 发送文件---建立socket
				socket = new Socket(ip, tcpPort);// 建立Socket连接
				//创建一个流套接字并将其连接到指定 IP 地址的指定端口号。
				OutputStream os = socket.getOutputStream();
				DataOutputStream dos = new DataOutputStream(os);
				/**
				 * 数据输出流允许应用程序以适当方式将基本 Java 数据类型写入输出流中。
				 */
				dos.writeUTF(chooser.getSelectedFile().getName());// 先发送文件名到对方电脑
				// 以与机器无关方式使用 UTF-8 修改版编码将一个字符串写入基础输出流。
				dos.flush();
				fis = new FileInputStream(fileName);
				int a = 0;
				byte by[] = new byte[1024 * 8];
				while ((a = fis.read(by)) != -1) {//从此输入流中将最多 b.length 个字节的数据读入一个 byte 数组中。
					dos.write(by, 0, a);//  将指定 byte 数组中从偏移量 off 开始的 len 个字节写入基础输出流。
					dos.flush();
				}
				/*
				 * OutputStreamWriter osw=new OutputStreamWriter(os); PrintWriter out=new
				 * PrintWriter(osw);
				 * out.println(chooser.getSelectedFile().getName());//先发送文件名到对方电脑----
				 * 在这里将文件的的名字发送出去了getName out.flush();//刷新缓冲区
				 */ /*
					 * FileInputStream fis=new FileInputStream(fileName); int a=0; byte by[]=new
					 * byte[1024*8]; while((a=fis.read(by))!=-1)//传送文件内容 {
					 * 
					 * }
					 */

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				try {
					fis.close();//关闭FileInpuStream
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}
	}
}