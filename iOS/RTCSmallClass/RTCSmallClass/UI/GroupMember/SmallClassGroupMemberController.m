//
//  SmallClassGroupMemberController.m
//  RTCSmallClass
//
//  Created by 孙震 on 2020/9/28.
//

#import "SmallClassGroupMemberController.h"
#import "SmallClassGroupMemberCell.h"
#import "NSBundle+RTCSmallClass.h"
#import "UIColor+Hex.h"
#import "UIImage+Color.h"
#import "RTCMacro.h"

@interface SmallClassGroupMemberController ()

@end


static NSString *CELLReuseId = @"SmallClassGroupMemberCell";

@implementation SmallClassGroupMemberController

- (instancetype)init
{
    UIStoryboard *storyboard = [NSBundle RSC_storyboard];
    return [storyboard instantiateViewControllerWithIdentifier:@"SmallClassGroupMemberController"];
}



- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.title = [NSString stringWithFormat:@"小组成员（%ld）",self.userList.count];
    
    [self.navigationController.navigationBar setTitleTextAttributes: @{NSForegroundColorAttributeName:[UIColor whiteColor]}];
    
    
    UIImage *backImage = [NSBundle RSC_pngImageWithName:@"back"];
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    [button setImage:backImage forState:UIControlStateNormal];
    [button addTarget:self action:@selector(back) forControlEvents:UIControlEventTouchUpInside];
    
    UIBarButtonItem *leftItem = [[UIBarButtonItem alloc] initWithCustomView:button];
    
    self.navigationItem.leftBarButtonItem = leftItem;
    UIImage *image = [UIImage imageWithColor:[UIColor colorWithHex:0x0E1117]];
    [self.navigationController.navigationBar setBackgroundImage:image forBarMetrics:UIBarMetricsDefault];
    [self.navigationController.navigationBar setShadowImage:image];
}

- (void)viewWillAppear:(BOOL)animated {
    [self.navigationController setNavigationBarHidden:NO];
    
    
}

- (void)viewWillDisappear:(BOOL)animated {
    [self.navigationController setNavigationBarHidden:YES];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.userList.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    SmallClassGroupMemberCell *cell = (SmallClassGroupMemberCell *)[tableView dequeueReusableCellWithIdentifier:CELLReuseId];
    SmallClassRemoteUser *user = self.userList[indexPath.item];
    cell.userName = user.displayName;
    cell.isMute = user.audioMuted;
    return cell;
    
}

- (void)back {
    [self.navigationController popViewControllerAnimated:YES];
}

@end
