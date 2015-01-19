package org.tuxdevelop.spring.batch.lightmin.model;


import lombok.Data;

import java.io.Serializable;

@Data
public class JobInfoModel implements Serializable{

   private String jobName;
   private Integer instanceCount;

}
