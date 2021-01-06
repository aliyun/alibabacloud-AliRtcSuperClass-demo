//
//  SmallClassChooseSubclassController.m
//  RTCSmallClass
//
//  Created by 孙震 on 2020/9/15.
//

#import "SmallClassChooseSubclassController.h"
#import "NSBundle+RTCSmallClass.h"
#import "RTCCommon.h"

@interface SmallClassChooseSubclassController ()

@property (assign,nonatomic) NSInteger selectedIndex;
@property (weak, nonatomic) UIButton *completeBtn;
@property (copy, nonatomic) void(^completeBlock)(NSInteger index,NSString *className);
@property (copy, nonatomic) NSArray *titles;

@end

@implementation SmallClassChooseSubclassController

- (instancetype)initWithDefaultIndex:(NSInteger)index
                                      complete:(void(^)(NSInteger index,NSString *className))completeBlock {
    UIStoryboard *storyboard = [NSBundle RSC_storyboard];
    self = [storyboard instantiateViewControllerWithIdentifier:@"SmallClassChooseSubclassController"];
    self.completeBlock = completeBlock;
    self.selectedIndex = index;
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupUI];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.selectedIndex = _selectedIndex;
    [self.navigationController setNavigationBarHidden:NO];
}


- (void)viewWillDisappear:(BOOL)animated {
    [self.navigationController setNavigationBarHidden:YES];
}
- (void)setupUI {
    
    UIBarButtonItem *leftItem = [[UIBarButtonItem alloc] initWithImage:[NSBundle RSC_pngImageWithName:@"angle_left"]
                                                                 style:UIBarButtonItemStylePlain
                                                                target:self
                                                                action:@selector(back)];
    self.navigationItem.leftBarButtonItem = leftItem;
    UILabel *titleView = [[UILabel alloc] init];
    titleView.text = @"选择小组";
    titleView.font = [UIFont systemFontOfSize:17];
    titleView.textColor = [UIColor colorWithHex:0x161A23];
    [titleView sizeToFit];
    self.navigationItem.titleView = titleView;
    
    
    UIButton *completeBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [completeBtn setTitle:@"完成" forState:UIControlStateNormal];
    completeBtn.bounds = CGRectMake(0, 0, 58, 27);
    [completeBtn.titleLabel setFont:[UIFont systemFontOfSize:13]];
    [completeBtn setBackgroundImage:[UIImage imageWithColor:[UIColor colorWithHex:0xe5e5e5]]
                           forState:UIControlStateDisabled];
    [completeBtn setBackgroundImage:[UIImage imageWithColor:[UIColor colorWithHex:0x013EBE]]
    forState:UIControlStateNormal];
    completeBtn.layer.cornerRadius = 2;
    [completeBtn addTarget:self action:@selector(complete)
          forControlEvents:UIControlEventTouchUpInside];
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:completeBtn];
    completeBtn.enabled = NO;
    self.completeBtn = completeBtn;
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
   
    self.selectedIndex = indexPath.row;
    
}


- (void)back {
    [self.navigationController popViewControllerAnimated:YES];
}


- (void)complete {
    if (self.completeBlock) {
        self.completeBlock(_selectedIndex,self.titles[_selectedIndex]);
    }
      [self.navigationController popViewControllerAnimated:YES];
}

- (void)setSelectedIndex:(NSInteger)selectedIndex {
   
    if (selectedIndex <0) {
        _selectedIndex = selectedIndex;
        return;
    }
    self.completeBtn.enabled = YES;
    
    UITableViewCell *oldCell = [self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:_selectedIndex inSection:0]];
    if (oldCell) {
        oldCell.accessoryType = UITableViewCellAccessoryNone;
    }
    
    UITableViewCell *newCell = [self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:selectedIndex inSection:0]];
    if (newCell) {
        newCell.accessoryType = UITableViewCellAccessoryCheckmark;
    }
    
    _selectedIndex = selectedIndex;
    
}

- (NSArray *)titles {
    if(!_titles) {
        _titles = @[@"A",@"B",@"C",@"D",@"E",@"F",@"G",@"H",@"I",@"J"];
    }
    return _titles;
}

@end
