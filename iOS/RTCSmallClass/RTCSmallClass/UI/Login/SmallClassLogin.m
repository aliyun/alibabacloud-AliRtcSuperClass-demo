//
//  SmallClassLogin.m
//  RTCSmallClass
//
//  Created by 孙震 on 2020/9/15.
//

#import "SmallClassLogin.h"
#import "NSBundle+RTCSmallClass.h"
#import "RTCCommon.h"
#import "RTCCommonView.h"
#import "SmallClassChooseSubclassController.h"
//#import "RTCSmallClass.h"
#import "SmallClassMainController.h"

@interface SmallClassLogin ()<UITextFieldDelegate>

@property (unsafe_unretained, nonatomic) IBOutlet UITextField *userNameTF;

@property (unsafe_unretained, nonatomic) IBOutlet UITextField *channelTF;

@property (unsafe_unretained, nonatomic) IBOutlet UIButton *subClassBtn;

@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *rightArrowImage;

@property (unsafe_unretained, nonatomic) IBOutlet LoadingButton *loginBtn;

@property (copy, nonatomic) NSString *subClassTitle;

@property (assign, nonatomic) NSInteger subClassIndex;

@property (copy, nonatomic) NSString *channel;

@property (copy, nonatomic) NSString *subChannel;

@property (copy, nonatomic) NSString *userName;
 

@end

@implementation SmallClassLogin

- (instancetype)init
{
    UIStoryboard *storyboard = [NSBundle RSC_storyboard];
    return [storyboard instantiateViewControllerWithIdentifier:@"SmallClassLogin"];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
  
    [self setupUI];
    self.subClassIndex = -1;
}
- (void)viewWillAppear:(BOOL)animated {
    [self.navigationController setNavigationBarHidden:NO];
    UIImage *image = [UIImage imageWithColor:[[UIColor whiteColor]colorWithAlphaComponent:0]];
    [self.navigationController.navigationBar setBackgroundImage:image forBarMetrics:UIBarMetricsDefault];
    [self.navigationController.navigationBar setShadowImage:image];
}

- (BOOL)shouldAutorotate {
    return NO;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
    return  UIInterfaceOrientationMaskPortrait;
}

- (UIStatusBarStyle) preferredStatusBarStyle {
    if (@available(iOS 13.0, *)) {
        return UIStatusBarStyleDarkContent;
    } else {
        return UIStatusBarStyleDefault;
    }
}


- (void)setupUI {
    //navigationbar
    self.navigationController.navigationBar.tintColor = [UIColor blackColor];
   
    
    UIBarButtonItem *leftItem = [[UIBarButtonItem alloc] initWithImage:[NSBundle RSC_pngImageWithName:@"angle_left"] style:UIBarButtonItemStylePlain target:self action:@selector(back)];
    self.navigationItem.leftBarButtonItem = leftItem;
    UILabel *titleView = [[UILabel alloc] init];
    titleView.text = @"";
    titleView.font = [UIFont systemFontOfSize:17];
    titleView.textColor = [UIColor colorWithHex:0x161A23];
    [titleView sizeToFit];
    self.navigationItem.titleView = titleView;
    
    self.rightArrowImage.image = [NSBundle RSC_pngImageWithName:@"rightArrow"];
    
    self.loginBtn.layer.cornerRadius = 24;
    
    [self.channelTF addTarget:self action:@selector(textChanged:) forControlEvents:UIControlEventEditingChanged];
}

- (void)back {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)chooseSubClass:(id)sender {
    [self endEditing];
    SmallClassChooseSubclassController *vc = [[SmallClassChooseSubclassController alloc] initWithDefaultIndex:_subClassIndex complete:^(NSInteger index, NSString * _Nonnull className) {
        self.subClassIndex = index;
        self.subClassTitle = className;
    }];
    
    
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)setSubClassTitle:(NSString *)subClassTitle {
    _subClassTitle = subClassTitle;
    [self.subClassBtn setTitle:[NSString stringWithFormat:@"%@ 组",subClassTitle]
                      forState:UIControlStateNormal];
}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self endEditing];
}

- (void)textChanged:(id)obj {
    if (self.channelTF.text.length >=5) {
        self.channelTF.text = [self.channelTF.text substringToIndex:5];
        [self endEditing];
    }
}
- (IBAction)login:(id)sender {

    [self endEditing];
    
    if( self.userNameTF.text.length ==0) {
        [RTCHUD showHud:@"请输入昵称" inView:self.view];
        return;
    }
   
    self.channel = self.channelTF.text;
    if (self.channel.length != 5) {
        [RTCHUD showHud:@"请输入5位房间号" inView:self.view];
        return;
    }
    
    if (self.subClassTitle.length==0) {
        [RTCHUD showHud:@"请选择小组" inView:self.view];
        return;
    }
    [self.loginBtn startLoading];
    self.subChannel = [NSString stringWithFormat:@"%@%@", self.channel,self.subClassTitle];
    self.userName = [NSString stringWithFormat:@"%@组_%@_学生",self.subClassTitle, self.userNameTF.text];
    

    SmallClassMainController *mainController = [[SmallClassMainController alloc] init];
    mainController.mainChannel = self.channel;
    mainController.subChannel = self.subChannel;
    mainController.displayName = self.userName;
    [self.navigationController pushViewController:mainController animated:YES];
    
    [self.loginBtn stopLoading];
}

- (void)endEditing {
    [self.view endEditing:YES];
}


@end
