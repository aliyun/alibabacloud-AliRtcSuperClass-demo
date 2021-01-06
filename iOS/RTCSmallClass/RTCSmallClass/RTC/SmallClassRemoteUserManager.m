//
//  RTCSampleRemoteUserManager.m
//  RtcSample
//
//  Created by Aliyun on 2019/3/22.
//  Copyright © 2019年 com.Alibaba. All rights reserved.
//

#import "SmallClassRemoteUserManager.h"
#import "SmallClassRemoteUser.h"

@interface SmallClassRemoteUserManager ()

/**
 @brief 远端用户
 */
@property(nonatomic, strong) NSMutableArray *remoteUsers;

/**
 @brief 操作锁
 */
@property(nonatomic,strong) NSRecursiveLock *arrLock;


@end


@implementation SmallClassRemoteUserManager

//+ (instancetype)shareManager {
//    static SmallClassRemoteUserManager *manager = nil;
//    static dispatch_once_t onceToken;
//    dispatch_once(&onceToken, ^{
//        if (manager == nil) {
//            manager = [[self alloc] init];
//        }
//    });
//    return manager;
//}

- (void)dealloc {
    NSLog(@"%s",__func__);
}

- (instancetype)init {
    self = [super init];
    if (self) {
        _remoteUsers = [NSMutableArray array];
        _arrLock = [[NSRecursiveLock alloc] init];
    }
    return self;
}


- (void)updateRemoteUser:(NSString *)uid forTrack:(AliRtcVideoTrack)track {
    
    [_arrLock lock]; 
    if (track == AliRtcVideoTrackBoth) {
        SmallClassRemoteUser *cameraModel = [self findUser:uid forTrack:AliRtcVideoTrackCamera];
        SmallClassRemoteUser *screenModel = [self findUser:uid forTrack:AliRtcVideoTrackScreen];
        SmallClassRemoteUser *noTrackModel = [self findUser:uid forTrack:AliRtcVideoTrackNo];
        
        if (!cameraModel) {
            cameraModel = [self createModel:uid forTrack:AliRtcVideoTrackCamera];
            [self.remoteUsers addObject:cameraModel];
        }
        
        if (!screenModel) {
            screenModel = [self createModel:uid forTrack:AliRtcVideoTrackScreen];
            [self.remoteUsers insertObject:screenModel atIndex:0];
        }
        
        if(noTrackModel) {
            screenModel.videoMuted = noTrackModel.videoMuted;
            screenModel.audioMuted = noTrackModel.audioMuted;
            cameraModel.videoMuted = noTrackModel.videoMuted;
            cameraModel.audioMuted = noTrackModel.audioMuted;
            [self.remoteUsers removeObject:noTrackModel];
        }
        
    }else if (track == AliRtcVideoTrackScreen) {
        SmallClassRemoteUser *cameraModel = [self findUser:uid forTrack:AliRtcVideoTrackCamera];
        SmallClassRemoteUser *screenModel = [self findUser:uid forTrack:AliRtcVideoTrackScreen];
        SmallClassRemoteUser *noTrackModel = [self findUser:uid forTrack:AliRtcVideoTrackNo];
        if (cameraModel) {
            [self.remoteUsers removeObject:cameraModel];
        }
        if (!screenModel) {
            screenModel = [self createModel:uid forTrack:AliRtcVideoTrackScreen];
            [self.remoteUsers insertObject:screenModel atIndex:0];
        }
        if(noTrackModel) {
            screenModel.videoMuted = noTrackModel.videoMuted;
            screenModel.audioMuted = noTrackModel.audioMuted;
            [self.remoteUsers removeObject:noTrackModel];
        }
        
    }else if (track == AliRtcVideoTrackCamera){
        SmallClassRemoteUser *cameraModel = [self findUser:uid forTrack:AliRtcVideoTrackCamera];
        SmallClassRemoteUser *screenModel = [self findUser:uid forTrack:AliRtcVideoTrackScreen];
        SmallClassRemoteUser *noTrackModel = [self findUser:uid forTrack:AliRtcVideoTrackNo];
        if (screenModel) {
            [self.remoteUsers removeObject:screenModel];
        }
        if (!cameraModel) {
            cameraModel = [self createModel:uid forTrack:AliRtcVideoTrackCamera];
            [self.remoteUsers addObject:cameraModel];
        }
        if(noTrackModel) {
            cameraModel.videoMuted = noTrackModel.videoMuted;
            cameraModel.audioMuted = noTrackModel.audioMuted;
            [self.remoteUsers removeObject:noTrackModel];
        }
    } else {
        //有可能不推流
        SmallClassRemoteUser *noTrackModel = [self findUser:uid forTrack:AliRtcVideoTrackNo];
        SmallClassRemoteUser *cameraModel = [self findUser:uid forTrack:AliRtcVideoTrackCamera];
        SmallClassRemoteUser *screenModel = [self findUser:uid forTrack:AliRtcVideoTrackScreen];
        if (screenModel) {
            [self.remoteUsers removeObject:screenModel];
        }
        if (cameraModel) {
            [self.remoteUsers removeObject:cameraModel];
        }
        if (!noTrackModel) {
            noTrackModel = [self createModel:uid forTrack:AliRtcVideoTrackNo];
            [self.remoteUsers addObject:noTrackModel];
        }
    }
    [_arrLock unlock];
}

- (void)updateRemoteUer:(NSString *)uid forDisplayName:(NSString *)displayName {
    [_arrLock lock];
     
    NSArray *users = [self findUser:uid];
    if(users.count == 0) {
        [self createModel:uid forTrack:AliRtcVideoTrackNo];
         users = [self findUser:uid];
    }
    for (SmallClassRemoteUser *user in users) {
        user.displayName = displayName;
    }

    [_arrLock unlock];
}

- (void)updateRemoteUer:(NSString *)uid forAudioMuted:(BOOL)audioMuted {
    [_arrLock lock];
    NSArray *users = [self findUser:uid];
    if(users.count == 0) {
        [self createModel:uid forTrack:AliRtcVideoTrackNo];
        users = [self findUser:uid];
    }
    
    for (SmallClassRemoteUser *user in users) {
        user.audioMuted = audioMuted;
    }
    [_arrLock unlock];
}

- (void)updateRemoteUer:(NSString *)uid forVideoMuted:(BOOL)videoMuted {
    [_arrLock lock];
    NSArray *users = [self findUser:uid];
    if(users.count == 0) {
        [self createModel:uid forTrack:AliRtcVideoTrackNo];
        users = [self findUser:uid];
    }
    for (SmallClassRemoteUser *user in users) {
        user.videoMuted = videoMuted;
    }
    [_arrLock unlock];
}

- (AliRenderView *)cameraView:(NSString *)uid {
    AliRenderView *rendView = nil;
    [_arrLock lock];
    for (SmallClassRemoteUser *model in self.remoteUsers) {
        if ([model.uid isEqualToString:uid] && model.track == AliRtcVideoTrackCamera) {
            rendView = model.view;
        }
    }
    [_arrLock unlock];
    return rendView;
}

- (AliRenderView *)screenView:(NSString *)uid {
    AliRenderView *rendView = nil;
    [_arrLock lock];
    for (SmallClassRemoteUser *model in self.remoteUsers) {
        if ([model.uid isEqualToString:uid] && model.track == AliRtcVideoTrackScreen) {
            rendView = model.view;
        }
    }
    [_arrLock unlock];
    return rendView;
}

- (void)remoteUserOffLine:(NSString *)uid {
    [_arrLock lock];
    NSArray *users = [self findUser:uid];
    for (SmallClassRemoteUser *user in users) {
        [self.remoteUsers removeObject:user];
    }
    
    [_arrLock unlock];
}


- (NSArray *)allOnlineUsers {
    return [self.remoteUsers copy];
}

- (void)removeAllUser {
    [_arrLock lock];
    [self.remoteUsers removeAllObjects];
    [_arrLock unlock];
}

- (void)removeUser:(SmallClassRemoteUser*)model {
    [_arrLock lock];
    [self.remoteUsers removeObject:model];
    [_arrLock unlock];
}



#pragma mark - private


/**
 @brief 创建用户流model
 
 @param uid 用户ID
 @param track 流类型
 @return 用户流model
 */
- (SmallClassRemoteUser *)createModel:(NSString *)uid forTrack:(AliRtcVideoTrack)track {
    if (uid.length == 0) {
        return nil;
    }
    SmallClassRemoteUser *model = [[SmallClassRemoteUser alloc] init];
    model.uid   = uid;
    model.track = track;
    model.view  = [[AliRenderView alloc] init];
    return model;
}



/**
 @brief 查找用户流
 
 @param uid 用户ID
 @param track 流类型
 @return 用户流model
 */
- (SmallClassRemoteUser *)findUser:(NSString *)uid forTrack:(AliRtcVideoTrack)track {
    
    for (SmallClassRemoteUser *model in self.remoteUsers) {
        if ([model.uid isEqualToString:uid] && (model.track == track)) {
            return model;
        }
    }
    return nil;
}

- (NSArray *)findUser:(NSString *)uid {
    
    NSPredicate *predict = [NSPredicate predicateWithFormat:@"SELF.uid like %@",uid];
    NSArray *users = [self.remoteUsers filteredArrayUsingPredicate:predict];
    
    return users;
}

@end
