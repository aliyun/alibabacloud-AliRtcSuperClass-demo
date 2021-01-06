//
//  SmallClassRemoteViewCell.m
//  RTCSmallClass
//
//  Created by 孙震 on 2020/10/12.
//

#import "SmallClassRemoteViewCell.h"
#import "NSBundle+RTCSmallClass.h"
#import "SmallClassRemoteUser.h"
#import "RTCMacro.h"

@interface SmallClassRemoteViewCell()

@property (unsafe_unretained, nonatomic) IBOutlet UIView *renderViewContainer;

@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *muteImageView;

@property (unsafe_unretained, nonatomic) IBOutlet UILabel *displayNameLabel;

@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *cameraIcon;

@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *micIcon;

@end

@implementation SmallClassRemoteViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self.cameraIcon setImage:[NSBundle RSC_pngImageWithName:@"camera_off"]];
    [self.micIcon setImage:[NSBundle RSC_pngImageWithName:@"mic_disabled"]];
    self.contentView.layer.borderWidth = 1;
}

- (void)setRemoteUser:(SmallClassRemoteUser *)remoteUser {
    _remoteUser = remoteUser;
    if (remoteUser) {
        //移除原来的renderView
        for (UIView *view in self.renderViewContainer.subviews) {
            if ([view isKindOfClass:[AliRenderView class]] && view != remoteUser.view) {
                [view removeFromSuperview];
            }
        }
        remoteUser.view.frame = CGRectMake(0, 0, 78, 78);
        [self.renderViewContainer addSubview:remoteUser.view];
        NSString *originName = remoteUser.displayName;
        self.displayNameLabel.text = [originName substringWithRange:NSMakeRange(0, originName.length - 3)];
    }
    remoteUser.view.hidden = remoteUser.videoMuted;
    
    //设置图片
    if(remoteUser.audioMuted) {
        [self.micIcon setImage:[NSBundle RSC_pngImageWithName:@"mic_off"]];
    } else {
        [self.micIcon setImage:[NSBundle RSC_pngImageWithName:@"mic_on"]];
    }
    if(remoteUser.isSpeaking) {
        self.contentView.layer.borderColor = RTCRGBA(92, 205, 232, 1).CGColor;
    } else {
        self.contentView.layer.borderColor = [UIColor clearColor].CGColor;
    }
    
}

@end    

