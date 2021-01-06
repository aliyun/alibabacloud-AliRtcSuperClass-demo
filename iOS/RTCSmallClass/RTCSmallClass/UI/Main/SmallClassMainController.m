//
//  SmallClassMainController.m
//  RTCSmallClass
//
//  Created by 孙震 on 2020/9/27.
//

#import "SmallClassMainController.h"
#import "NSBundle+RTCSmallClass.h"
#import "RTCSmallClass.h"
#import "SmallClassRemoteViewCell.h"
#import "SmallClassGroupMemberController.h"
#import "UIColor+Hex.h"
#import "RTCDoubleActionsAlertController.h"
#import "RTCSingleActionAlertController.h"
#import "RTCHUD.h"

@interface SmallClassMainController ()<RTCSmallClassDelegate,UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout>

@property (unsafe_unretained, nonatomic) IBOutlet UIButton *flipCameraBtn;

@property (unsafe_unretained, nonatomic) IBOutlet UILabel *channelLabel;

@property (unsafe_unretained, nonatomic) IBOutlet UIButton *channelCopyBtn;

@property (unsafe_unretained, nonatomic) IBOutlet UIButton *exitBtn;

@property (unsafe_unretained, nonatomic) IBOutlet UIButton *courseUnstart;

@property (unsafe_unretained, nonatomic) IBOutlet UIView *localPreview;

@property (unsafe_unretained, nonatomic) IBOutlet UIButton *muteBtn;

@property (unsafe_unretained, nonatomic) IBOutlet UILabel *muteLabel;

@property (unsafe_unretained, nonatomic) IBOutlet UIButton *cameraBtn;

@property (unsafe_unretained, nonatomic) IBOutlet UILabel *cameraLabel;

@property (unsafe_unretained, nonatomic) IBOutlet UILabel *connectLabel;

@property (unsafe_unretained, nonatomic) IBOutlet UIButton *connectingBtn;

@property (unsafe_unretained, nonatomic) IBOutlet UIButton *groupMemberBtn;

@property (unsafe_unretained, nonatomic) IBOutlet UIView *mainScreen;

@property (unsafe_unretained, nonatomic) IBOutlet UICollectionView *collectionView;

@property (nonatomic, strong) NSArray *remoteUserList;

@property (unsafe_unretained, nonatomic) IBOutlet UILabel *speakingLabel;

@property (unsafe_unretained, nonatomic) IBOutlet UIView *bottomView;

@property (nonatomic, strong) NSTimer *timer;

@property (nonatomic, assign) NSInteger countDown;

@property (unsafe_unretained, nonatomic) IBOutlet UIView *navigatorBar;

@property (unsafe_unretained, nonatomic) IBOutlet NSLayoutConstraint *collectionViewMarginTop;

@property (unsafe_unretained, nonatomic) IBOutlet UIButton *muteBtn2;

@property (unsafe_unretained, nonatomic) IBOutlet UIButton *rotationBtn;


@end

@implementation SmallClassMainController

- (instancetype)init
{
    UIStoryboard *storyboard = [NSBundle RSC_storyboard];
    return [storyboard instantiateViewControllerWithIdentifier:@"SmallClassMainController"];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [RTCSmallClass sharedInstance].delegate = self;
    
    [self setupUI];
    
    [[RTCSmallClass sharedInstance] login:self.mainChannel userName:self.displayName];
    [[RTCSmallClass sharedInstance] login:self.subChannel userName:self.displayName];
    
    [[RTCSmallClass sharedInstance] startPreview:self.localPreview];
    
    [self startTimer];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(deviceOrientationChange:)
                                                 name:UIDeviceOrientationDidChangeNotification object:nil];//屏幕旋转的通知
}

- (void)setupUI {
    //navigationbar
    [self.navigationController setNavigationBarHidden:YES];
    
    [self.flipCameraBtn setImage:[NSBundle RSC_pngImageWithName:@"flip_camera_disabled"] forState:UIControlStateDisabled];
    
    [self.flipCameraBtn setImage:[NSBundle RSC_pngImageWithName:@"flip_camera"] forState:UIControlStateNormal];
    [self.channelCopyBtn setImage:[NSBundle RSC_pngImageWithName:@"copy"] forState:UIControlStateNormal];
    [self.courseUnstart setImage:[NSBundle RSC_pngImageWithName:@"course_unstart"] forState:UIControlStateNormal];
    
    [self.muteBtn setImage:[NSBundle RSC_pngImageWithName:@"mic_on"] forState:UIControlStateNormal];
    [self.muteBtn setImage:[NSBundle RSC_pngImageWithName:@"mic_off"] forState:UIControlStateSelected];
    
    [self.muteBtn2 setImage:[NSBundle RSC_pngImageWithName:@"mute"] forState:UIControlStateNormal];
    [self.muteBtn2 setImage:[NSBundle RSC_pngImageWithName:@"cancel_mute"] forState:UIControlStateSelected];
    
    [self.cameraBtn setImage:[NSBundle RSC_pngImageWithName:@"camera_on"] forState:UIControlStateNormal];
    [self.cameraBtn setImage:[NSBundle RSC_pngImageWithName:@"camera_off"] forState:UIControlStateSelected];
    
    [self.connectingBtn setImage:[NSBundle RSC_pngImageWithName:@"disconnection"] forState:UIControlStateSelected];
    [self.connectingBtn setImage:[NSBundle RSC_pngImageWithName:@"connection"] forState:UIControlStateNormal];
    
    [self.groupMemberBtn setImage:[NSBundle RSC_pngImageWithName:@"groupMember"] forState:UIControlStateNormal];
    [self.rotationBtn setImage:[NSBundle RSC_pngImageWithName:@"h_v"] forState:UIControlStateNormal];
    
    UICollectionViewFlowLayout *layout = (UICollectionViewFlowLayout *)self.collectionView.collectionViewLayout;
    
    layout.minimumInteritemSpacing = 8;
    layout.minimumLineSpacing = 8;
    
    self.collectionView.delegate = self;
    self.collectionView.dataSource = self;
    self.channelLabel.text = [NSString stringWithFormat:@"教室码:%@",self.mainChannel];
    
}

- (UIStatusBarStyle) preferredStatusBarStyle {
    
    return UIStatusBarStyleLightContent;
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self reloadData];
    
    //翻转为竖屏
    [self setInterfaceOrientation:UIInterfaceOrientationPortrait];
    
}
// 刷新UI
- (void)reloadData {
    
    NSMutableArray *userList = @[].mutableCopy;
    
    NSArray *mainUserList = [[RTCSmallClass sharedInstance] getRemoteUserList:self.mainChannel];
    NSArray *subUserList =  [[RTCSmallClass sharedInstance] getRemoteUserList:self.subChannel];
    
    for (SmallClassRemoteUser *user in mainUserList) {
        if(user.track != AliRtcVideoTrackNo) {
            [userList addObject:user];
        }
    }
    
    for (SmallClassRemoteUser *user in subUserList) {
        if(user.track != AliRtcVideoTrackNo) {
            [userList addObject:user];
        }
    }
    //设置主屏 查找老师
    NSPredicate *predict = [NSPredicate predicateWithFormat:@"SELF.displayName ENDSWITH '老师'"];
    NSArray *users = [userList filteredArrayUsingPredicate:predict];
    //把老师放在前面
    for (SmallClassRemoteUser *teacher in users) {
        [userList removeObject:teacher];
        bool isScreen = teacher.track == AliRtcVideoTrackScreen;
        if (isScreen) {
            [userList insertObject:teacher atIndex:0];
        }else {
            SmallClassRemoteUser *firstUser = [userList firstObject];
            if (firstUser.track == AliRtcVideoTrackScreen) {
                [userList insertObject:teacher atIndex:1];
            }else {
                [userList insertObject:teacher atIndex:0];
            }
        }
    }
    
    SmallClassRemoteUser *mainUser = [userList firstObject];
    if (mainUser) {
        if([mainUser.displayName hasSuffix:@"老师"]){
            mainUser.view.frame = self.mainScreen.bounds;
            [self.mainScreen addSubview:mainUser.view];
            //设置远端
            NSMutableArray *users = userList.mutableCopy;
            [users removeObjectAtIndex:0];
            self.remoteUserList = [users copy];
            self.connectingBtn.enabled = YES;
            self.connectLabel.textColor = [UIColor whiteColor];
            [self.collectionView reloadData];
        } else {
            self.remoteUserList = userList.mutableCopy;
            self.connectingBtn.enabled = NO;
            self.connectLabel.textColor = [UIColor colorWithHex:0x888888];
            [[self.mainScreen subviews].firstObject removeFromSuperview];
            [self.collectionView reloadData];
        }
    } else {
        self.remoteUserList = nil;
        [self.collectionView reloadData];
        [[self.mainScreen subviews].firstObject removeFromSuperview];
        self.connectingBtn.enabled = NO;
        self.connectLabel.textColor = [UIColor colorWithHex:0x888888];
    }
}

#pragma mark - actions

- (IBAction)flipCamera:(id)sender {
    [[RTCSmallClass sharedInstance] switchCamera];
}
- (IBAction)channelCopy:(id)sender {
    //复制到剪切板
    UIPasteboard *board = [UIPasteboard generalPasteboard];
    board.string = self.mainChannel;
    [RTCHUD showHud:@"已复制" inView:self.view];
}

- (IBAction)exit:(id)sender {
    RTCDoubleActionsAlertController *vc = [[RTCDoubleActionsAlertController alloc]
                                           initWithTitle:@"离开教室"
                                           message:@"您确定离开教室吗？离开后，您可以用此教室码再次进入教室。"
                                           leftActionTitle:@"取消"
                                           leftAction:^{}
                                           rightActionTitle:@"确定离开"
                                           rightAction:^{
        
        [[RTCSmallClass sharedInstance] logout];
    }];
    
    [self presentViewController:vc animated:YES completion:nil];
}

- (IBAction)muteClick:(UIButton *)sender {
    if([[RTCSmallClass sharedInstance] muteLocalMic:!sender.selected channel:[self currentChannel]]  == 0) {
        self.muteBtn.selected = !self.muteBtn.selected;
        self.muteBtn2.selected =  self.muteBtn.selected ;
        if (self.muteBtn.selected) {
            self.muteLabel.text = @"取消静音";
        }else{
            self.muteLabel.text = @"静音";
        }
    }
}


- (IBAction)cameraClick:(UIButton*)sender {
    if (!sender.selected) {
        [[RTCSmallClass sharedInstance] muteLocalCamera:YES channel:[self currentChannel]];
        self.localPreview.hidden = YES;
        self.flipCameraBtn.enabled = NO;
        self.cameraLabel.text = @"开摄像头";
    }else {
        [[RTCSmallClass sharedInstance] muteLocalCamera:NO channel:[self currentChannel]];
        self.localPreview.hidden = NO;
        self.flipCameraBtn.enabled = YES;
        self.cameraLabel.text = @"关摄像头";
    }
    sender.selected = !sender.selected;
}

- (IBAction)connectionClick:(UIButton *)sender {
    //连麦要恢复原来的设置
    if (self.cameraBtn.selected) {
        [self cameraClick:self.cameraBtn];
    }
    
    if (self.muteBtn.selected) {
        [self muteClick:self.muteBtn];
    }
    
    if(sender.selected) {
        //主频道停止推流
        [[RTCSmallClass sharedInstance] stopPublish:self.mainChannel];
        //子频道开始推流
        [[RTCSmallClass sharedInstance] startPublish:self.subChannel];
    }else {
        //子频道停止推流
        [[RTCSmallClass sharedInstance] stopPublish:self.subChannel];
        //主频道开始推流
        [[RTCSmallClass sharedInstance] startPublish:self.mainChannel];
    }
    
    sender.selected = !sender.selected;
    
    if(sender.selected) {
        self.connectLabel.text = @"断开老师";
    }else {
        self.connectLabel.text = @"连麦老师";
    }
    
}

- (IBAction)groupMemberClick:(id)sender {
    SmallClassGroupMemberController *groupMember =  [[SmallClassGroupMemberController alloc] init];
    
    SmallClassRemoteUser *user = [[SmallClassRemoteUser alloc] init];
    user.displayName = [NSString stringWithFormat:@"%@(我)",self.displayName];
    user.audioMuted = self.muteBtn.selected;
    NSMutableArray *remoteUsers = [[[RTCSmallClass sharedInstance] getRemoteUserList:self.subChannel] mutableCopy];
    [remoteUsers insertObject:user atIndex:0];
    
    groupMember.userList = remoteUsers;
    
    [self.navigationController pushViewController:groupMember animated:YES];
}


- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.remoteUserList.count;
}

- (__kindof UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    SmallClassRemoteViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"SmallClassGroupMemberCell" forIndexPath:indexPath];
    SmallClassRemoteUser *user = self.remoteUserList[indexPath.item];
    cell.remoteUser = user;
    return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake(80, 80);
}

#pragma mark - RTCSmallClassDelegate

- (void)channel:(NSString *)channel onLeaveChannelResult:(int)result {
    if (result != 0) {
        NSString *info = [NSString stringWithFormat:@"%@离会失败",channel];
        [RTCHUD showHud:info inView:self.view];
    }
    
    [self onRoomdestroy];
}

- (void)channel:(NSString *)channel onUserAudioMuted:(NSString *)uid audioMuted:(BOOL)isMute {
    [self reloadData];
}

- (void)channel:(NSString *)channel onUserVideoMuted:(NSString *)uid videoMuted:(BOOL)isMute {
    [self reloadData];
}

- (void)channel:(NSString *)channel onRemoteTrace:(NSString *)uid avaliable:(BOOL)abaliable {
    [self reloadData];
}

- (void)channel:(NSString *)channel onSubscribe:(NSString *)uid result:(BOOL)result {
    [self reloadData];
}

- (void)channel:(NSString *)channel onUserSpeaking:(NSString *)uid speaking:(BOOL)speaking displayName:(nonnull NSString *)name{
    NSLog(@"频道:%@---用户:%@--说话:%d",channel,uid,speaking);
    [self reloadData];
    //子频道中显示人员说话信息
    if(![channel isEqualToString:self.mainChannel]) {
        //更新说话的Label
        NSString *text = self.speakingLabel.text ;
        text = [text stringByReplacingOccurrencesOfString:@" 正在发言" withString:@""];
        if (speaking) {
            text = [NSString stringWithFormat:@"%@ %@ 正在发言",text, name];
            self.speakingLabel.text = text;
        } else {
            text = [text stringByReplacingOccurrencesOfString:name withString:@""];
            if ([text stringByReplacingOccurrencesOfString:@" " withString:@""].length) {
                self.speakingLabel.text = [NSString stringWithFormat:@"%@ 正在发言",text];
            } else {
                self.speakingLabel.text = @"";
            }
        }
    }
}

- (void)channel:(NSString *)channel onSDKError:(int)error {
    NSString *warnStr = [NSString stringWithFormat:@"%@频道发生严重错误%d",channel,error];
    
    RTCSingleActionAlertController *vc = [[RTCSingleActionAlertController alloc] initWithTitle:@"错误" message:warnStr actionTitle:@"确定" action:^{
        [self onRoomdestroy];
    }];
    [self presentViewController:vc animated:YES completion:nil];
}

- (void)channel:(NSString *)channel onOccurError:(int)error {
    NSString *warnStr = [NSString stringWithFormat:@"%@频道发生错误%d",channel,error];
    
    RTCSingleActionAlertController *vc = [[RTCSingleActionAlertController alloc] initWithTitle:@"错误" message:warnStr actionTitle:@"确定" action:^{
    }];
    [self presentViewController:vc animated:YES completion:nil];
}

- (void)channel:(NSString *)channel onOccurWarning:(int)warn {
    NSString *warnStr = [NSString stringWithFormat:@"%@频道发生警告%d",channel,warn];
    RTCSingleActionAlertController *vc = [[RTCSingleActionAlertController alloc] initWithTitle:@"警告" message:warnStr actionTitle:@"确定" action:^{
    }];
    [self presentViewController:vc animated:YES completion:nil];
}

/// 房间被销毁通知
- (void)onRoomdestroy {
    [[RTCSmallClass sharedInstance] destroySharedInstance];
    [self.navigationController popViewControllerAnimated:YES];
}


- (void)channel:(NSString *)channel onJoinChannelResult:(int)result  {
    if(result != 0) {
        NSString *info = [NSString stringWithFormat:@"房间加入失败：%@",channel];
        [RTCHUD showHud:info inView:self.view];
    }
}
#pragma mark -timer

- (void)startTimer {
    self.timer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(count:) userInfo:nil repeats:YES];
    self.countDown = 5;
    [self showToolBar];
    [self.timer fire];
}
- (void)count:(NSTimer *) timer {
    self.countDown--;
    if (self.countDown == 0) {
        [self hideToolBar];
    }
    
}
- (void)destroyTimer {
    [self.timer invalidate];
    self.timer = nil;
}


- (void)hideToolBar {
    self.bottomView.hidden = YES;
//    self.localPreview.hidden = YES;
    self.navigatorBar.hidden = YES;
    self.muteBtn2.hidden = NO;
    self.rotationBtn.hidden= YES;
    self.collectionViewMarginTop.constant = 8-44;
}

- (void)showToolBar {
    self.bottomView.hidden = NO;
//    self.localPreview.hidden =  self.cameraBtn.selected;
    self.navigatorBar.hidden = NO;
    self.muteBtn2.hidden = YES;
    self.rotationBtn.hidden = NO;
    self.collectionViewMarginTop.constant = 8;
}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self showToolBar];
    self.countDown = 5;
}

- (void)deviceOrientationChange:(NSNotification *)notification {
    
    UICollectionViewFlowLayout *layout = (UICollectionViewFlowLayout *)self.collectionView.collectionViewLayout;
    
    UIDeviceOrientation interfaceOrientation = [UIDevice currentDevice].orientation;
    if (interfaceOrientation == UIDeviceOrientationPortrait || interfaceOrientation == UIDeviceOrientationPortraitUpsideDown) {
        //翻转为竖屏时
        layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
        [self reloadData];
        
    }else if (interfaceOrientation==UIDeviceOrientationLandscapeLeft || interfaceOrientation == UIDeviceOrientationLandscapeRight) {
        //翻转为横屏时
        layout.scrollDirection = UICollectionViewScrollDirectionVertical;
        [self reloadData];
    }
    
}

- (IBAction)rotate:(id)sender {
    UIDeviceOrientation interfaceOrientation = [UIDevice currentDevice].orientation;
    if (interfaceOrientation == UIDeviceOrientationPortrait || interfaceOrientation == UIDeviceOrientationPortraitUpsideDown) {
        //翻转为横屏
        [self setInterfaceOrientation:UIInterfaceOrientationLandscapeRight];
    }else if (interfaceOrientation==UIDeviceOrientationLandscapeLeft || interfaceOrientation == UIDeviceOrientationLandscapeRight) {
        //翻转为竖屏
        [self setInterfaceOrientation:UIInterfaceOrientationPortrait];
    }
}


//强制转屏
- (void)setInterfaceOrientation:(UIInterfaceOrientation)orientation{
    
    if ([[UIDevice currentDevice] respondsToSelector:@selector(setOrientation:)]) {
        SEL selector  = NSSelectorFromString(@"setOrientation:");
        NSInvocation *invocation = [NSInvocation invocationWithMethodSignature:[UIDevice instanceMethodSignatureForSelector:selector]];
        [invocation setSelector:selector];
        [invocation setTarget:[UIDevice currentDevice]];
        // 从2开始是因为前两个参数已经被selector和target占用
        [invocation setArgument:&orientation atIndex:2];
        [invocation invoke];
    }
}


- (NSString *)currentChannel {
    if (self.connectingBtn.selected) {
        return self.mainChannel;
    } else {
        return self.subChannel;
    }
}
@end
