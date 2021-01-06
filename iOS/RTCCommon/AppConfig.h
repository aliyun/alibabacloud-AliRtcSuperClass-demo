//
//  AppConfig.h
//  LectureHall
//
//  Created by Aliyun on 2020/5/22.
//  Copyright © 2020 alibaba. All rights reserved.
//

#ifndef AppConfig_h
#define AppConfig_h

//====================================  URL 常量 start =========================================================================

#define kBaseUrl                                @""

#define kProject_1v1Audio                       @"1v1-audio"
#define kProject_InteractiveClass               @"interactive-live-class"
#define kProject_Chatroom                       @"chatroom"
#define kProject_VideoRoom                      @"live-pk"
#define kProject_SmallClass                     @"live-pk"


#define kBaseUrl_1v1Audio                       [NSString stringWithFormat:@"%@%@/",kBaseUrl,kProject_1v1Audio]
#define kBaseUrl_InteractiveClass               [NSString stringWithFormat:@"%@%@/",kBaseUrl,kProject_InteractiveClass]
#define kBaseUrl_Chatroom                       [NSString stringWithFormat:@"%@%@/",kBaseUrl,kProject_Chatroom]
#define kBaseUrl_VideoRoom                      [NSString stringWithFormat:@"%@%@/",kBaseUrl,kProject_VideoRoom]

#define kBaseUrl_SmallClass                     @""
 
                             

//=====================================  URL 常量 end ==========================================================================

#endif /* AppConfig_h */
