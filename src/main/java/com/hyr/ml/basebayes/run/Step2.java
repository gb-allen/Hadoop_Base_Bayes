package com.hyr.ml.basebayes.run;

import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.hyr.ml.basebayes.mr.WordOfLabelCountMapper;
import com.hyr.ml.basebayes.mr.WordOfLabelCountReducer;

/**
 * 统计词在不同分类出现的次数
 * 不错:0 1
 * 词:词所在文档的分类标签  次数
 * @author root
 */
public class Step2 {

	public static boolean run(Configuration config, Map<String, String> paths) {
		try {
			FileSystem fs = FileSystem.get(config);
			Job job = Job.getInstance(config);
			
			job.setJobName("step2");

//			config.set("mapred.jar", "D:\\MR\\item.jar");

			job.setJarByClass(Step2.class);
			job.setMapperClass(WordOfLabelCountMapper.class);
			job.setReducerClass(WordOfLabelCountReducer.class);

			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(IntWritable.class);

			FileInputFormat.addInputPath(job, new Path(paths.get("Step2Input")));
			
			Path outpath = new Path(paths.get("Step2Output"));
			if (fs.exists(outpath)) {
				fs.delete(outpath, true);
			}
			FileOutputFormat.setOutputPath(job, outpath);

			boolean f = job.waitForCompletion(true);
			return f;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}

