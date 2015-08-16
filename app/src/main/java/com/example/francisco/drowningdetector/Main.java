//package com.example.francisco.drowningdetector;
//
//import android.graphics.Bitmap;
//
//import java.awt.Toolkit;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Vector;
//
//import javax.imageio.ImageIO;
//import javax.swing.ImageIcon;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//
//import org.opencv.core.Core;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfByte;
//import org.opencv.core.MatOfPoint;
//import org.opencv.core.Point;
//import org.opencv.core.Rect;
//import org.opencv.core.Scalar;
//import org.opencv.core.Size;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.video.BackgroundSubtractorMOG2;
//import org.opencv.video.Video;
//import org.opencv.videoio.VideoCapture;
//
//
//public class Main {
//	static {
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//		// System.loadLibrary("opencv_java2410");
//	}
//
//	static Mat imag = null;
//	static Mat orgin = null;
//	static Mat kalman = null;
//	public static Tracker tracker;
//
//	public static void main(String[] args) throws InterruptedException {
//
//		if (args.length>0){
//			CONFIG.filename = args[0];
//		}
//
//		JFrame jFrame = new JFrame("MULTIPLE-TARGET TRACKING");
//		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		JLabel vidpanel = new JLabel();
//		jFrame.setContentPane(vidpanel);
//		jFrame.setSize(CONFIG.FRAME_WIDTH, CONFIG.FRAME_HEIGHT);
//		jFrame.setLocation((3 / 4)
//				* Toolkit.getDefaultToolkit().getScreenSize().width, (3 / 4)
//				* Toolkit.getDefaultToolkit().getScreenSize().height);
//		jFrame.setVisible(true);
//
//		// ////////////////////////////////////////////////////////
//		JFrame jFrame2 = new JFrame("BACKGROUND SUBSTRACTION");
//		jFrame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		JLabel vidpanel2 = new JLabel();
//		jFrame2.setContentPane(vidpanel2);
//		jFrame2.setSize(CONFIG.FRAME_WIDTH, CONFIG.FRAME_HEIGHT);
//		jFrame2.setLocation(
//				Toolkit.getDefaultToolkit().getScreenSize().width / 2, (3 / 4)
//						* Toolkit.getDefaultToolkit().getScreenSize().height);
//		jFrame2.setVisible(true);
//		// ////////////////////////////////////////////////////////
///*
//		// ////////////////////////////////////////////////////////
//		JFrame jFrame3 = new JFrame("VIDEO INPUT");
//		jFrame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		JLabel vidpanel3 = new JLabel();
//		jFrame3.setContentPane(vidpanel3);
//		jFrame3.setSize(CONFIG.FRAME_WIDTH, CONFIG.FRAME_HEIGHT);
//		jFrame3.setLocation((3 / 4)
//				* Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit
//				.getDefaultToolkit().getScreenSize().height / 2);
//		jFrame3.setVisible(true);
//		// ////////////////////////////////////////////////////////
//
//		// ////////////////////////////////////////////////////////
//		JFrame jFrame4 = new JFrame("KALMAN FILTER");
//		jFrame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		JLabel vidpanel4 = new JLabel();
//		jFrame4.setContentPane(vidpanel4);
//		jFrame4.setSize(CONFIG.FRAME_WIDTH, CONFIG.FRAME_HEIGHT);
//		jFrame4.setLocation(
//				Toolkit.getDefaultToolkit().getScreenSize().width / 2, Toolkit
//						.getDefaultToolkit().getScreenSize().height / 2);
//		jFrame4.setVisible(false);
//		// ////////////////////////////////////////////////////////
//*/
//		Mat frame = new Mat();
//		Mat outbox = new Mat();
//		Mat diffFrame = null;
//		Vector<Rect> array = new Vector<Rect>();
//
//		BackgroundSubtractorMOG2 mBGSub = Video
//				.createBackgroundSubtractorMOG2();
//
//		tracker = new Tracker((float) CONFIG._dt,
//				(float) CONFIG._Accel_noise_mag, CONFIG._dist_thres,
//				CONFIG._maximum_allowed_skipped_frames,
//				CONFIG._max_trace_length);
//
//		// Thread.sleep(1000);
//		VideoCapture camera = new VideoCapture("visiontraffic.avi");
//		//camera.open(CONFIG.filename);
//		// VideoCapture camera = new VideoCapture(0);
//		int i = 0;
//
//		if (!camera.isOpened()) {
//			System.out.print("Can not open Camera, try it later.");
//			return;
//		}
//
//		while (true) {
//			if (!camera.read(frame))
//				break;
//			Imgproc.resize(frame, frame, new Size(CONFIG.FRAME_WIDTH, CONFIG.FRAME_HEIGHT),
//					0., 0., Imgproc.INTER_LINEAR);
//			imag = frame.clone();
//			orgin = frame.clone();
//			kalman = frame.clone();
//			if (i == 0) {
//				// jFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
//				diffFrame = new Mat(outbox.size(), CvType.CV_8UC1);
//				diffFrame = outbox.clone();
//			}
//
//			if (i == 1) {
//				diffFrame = new Mat(frame.size(), CvType.CV_8UC1);
//				processFrame(camera, frame, diffFrame, mBGSub);
//				frame = diffFrame.clone();
///*
//				array = detectionContours(diffFrame);
//				// ///////
//				Vector<Point> detections = new Vector<>();
//				// detections.clear();
//				Iterator<Rect> it = array.iterator();
//				while (it.hasNext()) {
//					Rect obj = it.next();
//
//					int ObjectCenterX = (int) ((obj.tl().x + obj.br().x) / 2);
//					int ObjectCenterY = (int) ((obj.tl().y + obj.br().y) / 2);
//
//					Point pt = new Point(ObjectCenterX, ObjectCenterY);
//					detections.add(pt);
//				}
//*/
//				// ///////
///*
//				if (array.size() > 0) {
//					tracker.update(array, detections, imag);
//					Iterator<Rect> it3 = array.iterator();
//					while (it3.hasNext()) {
//						Rect obj = it3.next();
//
//						int ObjectCenterX = (int) ((obj.tl().x + obj.br().x) / 2);
//						int ObjectCenterY = (int) ((obj.tl().y + obj.br().y) / 2);
//
//						Point pt = new Point(ObjectCenterX, ObjectCenterY);
//
//						Imgproc.rectangle(imag, obj.br(), obj.tl(), new Scalar(
//								0, 255, 0), 2);
//						Imgproc.circle(imag, pt, 1, new Scalar(0, 0, 255), 2);
//					}
//				} else if (array.size() == 0) {
//					tracker.updateKalman(imag, detections);
//				}
//
//				for (int k = 0; k < tracker.tracks.size(); k++) {
//					int traceNum = tracker.tracks.get(k).trace.size();
//					if (traceNum > 1) {
//						for (int jt = 1; jt < tracker.tracks.get(k).trace
//								.size(); jt++) {
//							Imgproc.line(imag,
//									tracker.tracks.get(k).trace.get(jt - 1),
//									tracker.tracks.get(k).trace.get(jt),
//									CONFIG.Colors[tracker.tracks.get(k).track_id % 9],
//									2, 4, 0);
//						}
//					}
//				}
//*/
///*
//				Imgproc.putText(imag, "Input: " + CONFIG.filename, new Point(20, 360),
//						Core.FONT_HERSHEY_PLAIN, 1, new Scalar(255, 255, 255),
//						1);
//				Imgproc.putText(imag,
//						"So track hien tai: " + tracker.tracks.size()
//								+ "     Da xoa: " + tracker.track_removed,
//						new Point(20, 50), Core.FONT_HERSHEY_PLAIN, 1,
//						new Scalar(255, 255, 255), 1); */
//			}
//
//			i = 1;
//
//			ImageIcon image = new ImageIcon(Mat2bufferedImage(imag));
//			vidpanel.setIcon(image);
//			vidpanel.repaint();
//			// temponFrame = outerBox.clone();
//
//			ImageIcon image2 = new ImageIcon(Mat2bufferedImage(frame));
//			vidpanel2.setIcon(image2);
//			vidpanel2.repaint();
//
//			ImageIcon image3 = new ImageIcon(Mat2bufferedImage(orgin));
//			vidpanel3.setIcon(image3);
//			vidpanel3.repaint();
///*
//			ImageIcon image4 = new ImageIcon(Mat2bufferedImage(kalman));
//			vidpanel4.setIcon(image4);
//			vidpanel4.repaint();
//*/
//		}
//
//	}
//
//	// background substractionMOG2
//	protected static void processFrame(VideoCapture capture, Mat mRgba,
//									   Mat mFGMask, BackgroundSubtractorMOG2 mBGSub) {
//		// GREY_FRAME also works and exhibits better performance
//		mBGSub.apply(mRgba, mFGMask, CONFIG.learningRate);
//		Imgproc.cvtColor(mFGMask, mRgba, Imgproc.COLOR_GRAY2BGRA, 0);
//		Mat erode = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(
//				8, 8));
//		Mat dilate = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
//				new Size(8, 8));
//
//		Mat openElem = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
//				new Size(3, 3), new Point(1, 1));
//		Mat closeElem = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
//				new Size(7, 7), new Point(3, 3));
//
//		Imgproc.threshold(mFGMask, mFGMask, 127, 255, Imgproc.THRESH_BINARY);
//		Imgproc.morphologyEx(mFGMask, mFGMask, Imgproc.MORPH_OPEN, erode);
//		Imgproc.morphologyEx(mFGMask, mFGMask, Imgproc.MORPH_OPEN, dilate);
//		Imgproc.morphologyEx(mFGMask, mFGMask, Imgproc.MORPH_OPEN, openElem);
//		Imgproc.morphologyEx(mFGMask, mFGMask, Imgproc.MORPH_CLOSE, closeElem);
//	}
//
//	private static Bitmap Mat2bufferedImage(Mat image) {
//		MatOfByte bytemat = new MatOfByte();
//		Imgcodecs.imencode(".jpg", image, bytemat);
//		byte[] bytes = bytemat.toArray();
//		InputStream in = new ByteArrayInputStream(bytes);
//		BufferedImage img = null;
//		try {
//			img = ImageIO.read(in);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return img;
//	}
///*
//	public static Vector<Rect> detectionContours(Mat outmat) {
//		Mat v = new Mat();
//		Mat vv = outmat.clone();
//		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//		Imgproc.findContours(vv, contours, v, Imgproc.RETR_LIST,
//				Imgproc.CHAIN_APPROX_SIMPLE);
//
//		int maxAreaIdx = -1;
//		Rect r = null;
//		Vector<Rect> rect_array = new Vector<Rect>();
//
//		for (int idx = 0; idx < contours.size(); idx++) {
//			Mat contour = contours.get(idx);
//			double contourarea = Imgproc.contourArea(contour);
//			if (contourarea > CONFIG.MIN_BLOB_AREA && contourarea < CONFIG.MAX_BLOB_AREA) {
//				// MIN_BLOB_AREA = contourarea;
//				maxAreaIdx = idx;
//				r = Imgproc.boundingRect(contours.get(maxAreaIdx));
//				rect_array.add(r);
//				// Imgproc.drawContours(imag, contours, maxAreaIdx, new
//				// Scalar(255, 255, 255));
//			}
//
//		}
//
//		v.release();
//		return rect_array;
//	}
//
//
//}
