//
//  RTCSampleRemoteUserModel.h
//  RtcSample
//
//  Created by daijian on 2019/4/11.
//  Copyright © 2019年 tiantian. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AliRTCSdk/AliRTCSdk.h>

NS_ASSUME_NONNULL_BEGIN

@interface SmallClassRemoteUser : NSObject

@property (nonatomic, strong) AliRenderView *view;

@property (nonatomic, strong) NSString *uid;

@property (nonatomic, assign) AliRtcVideoTrack track;

@property (nonatomic, assign) BOOL audioMuted;

@property (nonatomic, assign) BOOL videoMuted;

@property (nonatomic, copy) NSString *displayName;

@property (nonatomic, assign) BOOL isSpeaking; 

@end

NS_ASSUME_NONNULL_END
