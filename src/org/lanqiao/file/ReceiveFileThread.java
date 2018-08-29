package org.lanqiao.file;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFileChooser;

public class ReceiveFileThread implements Runnable {

	ServerSocket ss = null;
	public int port = 8888;

	public ReceiveFileThread() {
		while (true) {
			try {
				ss = new ServerSocket(port);
				break;
			} catch (IOException e) {
				port += 1;
				// continue;这个continue可以要也可以不要
			}
		}
	}

	public void run() {
		while (true) {
			Socket socket = null;
			FileOutputStream fos = null;
			/**
			 * 文件输出流是用于将数据写入 File 或 FileDescriptor 的输出流。 用于写入诸如图像数据之类的原始字节的流。
			 */
			try {
				socket = ss.accept();// 监听客户端的数据
				InputStream is = socket.getInputStream();
				/*
				 * InputStreamReader isr=new InputStreamReader(is); BufferedReader br=new
				 * BufferedReader(isr); String fileName=br.readLine();
				 */
				// 换另一种方法来发送文件
				DataInputStream dis = new DataInputStream(is);
				/**
				 * DataInputStream 数据输入流允许应用程序以与机器无关方式从底层输入流中读取基本 Java 数据类型。 对于多线程的访问不一定安全
				 */
				String fileName = dis.readUTF();// 从包含的输入流中读取此操作需要的字节。
				System.out.println("文件名是：" + fileName);
				// 读取对方发来的文件，并且写入本地硬盘
				// 弹出保存文件窗口
				File file = new File(fileName);
				JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(file);
				int returnVal = chooser.showSaveDialog(null);
				byte by[] = new byte[1024 * 8];
				int a = 0;
				fos = new FileOutputStream(chooser.getSelectedFile().getAbsolutePath());
				// 参数为文件的路径 创建一个向具有指定名称的文件中写入数据的输出文件流。

				if (returnVal == JFileChooser.APPROVE_OPTION) {// 如果点了Save按钮
					while ((a = dis.read(by)) != -1) {// 传送文件内容
						fos.write(by, 0, a);
						fos.flush();

					}
				}
			} catch (IOException e) {
			} finally {
				try {
					fos.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}
}