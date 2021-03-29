//
//  ViewController.m
//  MidObjcSample
//
//  Created by Muhammad Fauzi Masykur on 07/08/20.
//  Copyright © 2020 Muhammad Fauzi Masykur. All rights reserved.
//

#import "ViewController.h"
#import <MidtransKit/MidtransKit.h>
#import "ResultViewController.h"

@interface ViewController () <MidtransUIPaymentViewControllerDelegate>

@property (weak, nonatomic) IBOutlet UITextField *snaptokenTextField;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    //setup callback url, to enable customer to go back to this app after completing payment in e-wallet app (gojek/shopeepay).
    //setup gopay callback url
    [MidtransConfig shared].callbackSchemeURL = @"sampleapp-objc://";
    //setup shopeepay callback url
    [MidtransConfig shared].shopeePayCallbackURL = @"sampleapp-objc://";
}

- (NSString *)generateRandomOrderId{
    NSDate * now = [NSDate date];
    NSDateFormatter *outputFormatter = [[NSDateFormatter alloc] init];
    [outputFormatter setDateFormat:@"HHmmss"];
    NSString *randomOrderIdFromDate = [outputFormatter stringFromDate:now];
    return randomOrderIdFromDate;
}

- (void)showSnaptokenEmptyAlert{
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Snaptoken is empty" message:@"please input a snaptoken to use a snaptoken payment" preferredStyle:UIAlertControllerStyleAlert];
    [alert addAction:[UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleCancel handler:nil]];
    [self presentViewController:alert animated:YES completion:nil];
}

- (void)showPaymentStatusPage:(NSString *)paymentStatus{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    ResultViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"resultVC"];
    vc.paymentStatus = paymentStatus;
    [self.navigationController pushViewController:vc animated:YES];
}

- (IBAction)payWithSnaptokenFlow:(id)sender {
    if (self.snaptokenTextField.text.length != 0) {
        [[MidtransMerchantClient shared]requestTransacationWithCurrentToken:self.snaptokenTextField.text completion:^(MidtransTransactionTokenResponse * _Nullable response, NSError * _Nullable error) {
            if (response) {
                //present payment page
                MidtransUIPaymentViewController *paymentVC = [[MidtransUIPaymentViewController alloc] initWithToken:response];
                paymentVC.paymentDelegate = self;
                [self presentViewController:paymentVC animated:YES completion:nil];
            } else {
                NSLog(@"error %@", error);
            }
        }];
    } else {
        [self showSnaptokenEmptyAlert];
    }
}
- (IBAction)payWithSDKFlow:(id)sender {
    
    //Create Transaction Details
    MidtransTransactionDetails *transactionDetails = [[MidtransTransactionDetails alloc]initWithOrderID:[self generateRandomOrderId] andGrossAmount:@300];
    MidtransItemDetail *itemJacket = [[MidtransItemDetail alloc]initWithItemID:@"item1" name:@"jacket" price:@150 quantity:@1];
    MidtransItemDetail *itemPants = [[MidtransItemDetail alloc]initWithItemID:@"item2" name:@"pants" price:@150 quantity:@1];
    MidtransAddress *address = [MidtransAddress addressWithFirstName:@"john" lastName:@"doe" phone:@"082221009999" address:@"jl.buntu 2" city:@"Jakarta" postalCode:@"112233" countryCode:@"IDN"];
    MidtransCustomerDetails *customerDetails = [[MidtransCustomerDetails alloc]initWithFirstName:@"John" lastName:@"Doe" email:@"cihuy@cihuyhuy.com" phone:@"082221009999" shippingAddress:address billingAddress:address];
    
    //request transaction token
    [[MidtransMerchantClient shared] requestTransactionTokenWithTransactionDetails:transactionDetails itemDetails:@[itemJacket,itemPants] customerDetails:customerDetails completion:^(MidtransTransactionTokenResponse * _Nullable token, NSError * _Nullable error) {
        
        if (token) {
        //present payment page
        MidtransUIPaymentViewController *paymentVC = [[MidtransUIPaymentViewController alloc] initWithToken:token];
        paymentVC.paymentDelegate = self;
        [self presentViewController:paymentVC animated:YES completion:nil];
        } else {
            NSLog(@"error %@", error);
        }
    }];
}

//PRAGMA MARK: - MidtransUIPaymentViewControllerDelegate
//Transaction callback/delegates to handle all the transaction status
- (void)paymentViewController:(MidtransUIPaymentViewController *)viewController paymentFailed:(NSError *)error {
    NSLog(@"payment failed");
    [self showPaymentStatusPage:@"error"];
}

- (void)paymentViewController:(MidtransUIPaymentViewController *)viewController paymentPending:(MidtransTransactionResult *)result {
    NSLog(@"payment pending");
    [self showPaymentStatusPage:result.transactionStatus];
}

- (void)paymentViewController:(MidtransUIPaymentViewController *)viewController paymentSuccess:(MidtransTransactionResult *)result {
    NSLog(@"payment success");
    [self showPaymentStatusPage:result.transactionStatus];
}

- (void)paymentViewController_paymentCanceled:(MidtransUIPaymentViewController *)viewController {
    NSLog(@"payment cancelled");
    [self showPaymentStatusPage:@"cancelled"];
}
-(void)paymentViewController:(MidtransUIPaymentViewController *)viewController paymentDeny:(MidtransTransactionResult *)result {
    NSLog(@"payment denied");
    [self showPaymentStatusPage:result.transactionStatus];
}

@end
