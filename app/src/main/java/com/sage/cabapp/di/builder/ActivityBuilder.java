package com.sage.cabapp.di.builder;

import com.sage.cabapp.di.module.AppModule;
import com.sage.cabapp.ui.accountsettings.AccountSettingsActivity;
import com.sage.cabapp.ui.addbusinessprofile.AddBusinessProfileActivity;
import com.sage.cabapp.ui.addbusinessprofile.NewBusinessProfileActivity;
import com.sage.cabapp.ui.addemailsignup.AddEmailSignupActivity;
import com.sage.cabapp.ui.addhomeplace.AddHomePlaceActivity;
import com.sage.cabapp.ui.addnamesignup.AddNameSignupActivity;
import com.sage.cabapp.ui.addpaymentmethod.AddPaymentMethodActivity;
import com.sage.cabapp.ui.addpromocode.AddPromoCodeActivity;
import com.sage.cabapp.ui.addriverrating.AddDriverRatingActivity;
import com.sage.cabapp.ui.addrivertip.AddDriverTipActivity;
import com.sage.cabapp.ui.addworkplace.AddWorkPlaceActivity;
import com.sage.cabapp.ui.chatmodulenew.ChatModuleNewActivity;
import com.sage.cabapp.ui.confirmpickup.ConfirmPickupMapActivity;
import com.sage.cabapp.ui.driverprofile.DriverProfileActivity;
import com.sage.cabapp.ui.editbusinessprofile.EditBusinessProfileActivity;
import com.sage.cabapp.ui.editcard.EditCardActivity;
import com.sage.cabapp.ui.editemail.EditEmailActivity;
import com.sage.cabapp.ui.editname.EditNameActivity;
import com.sage.cabapp.ui.editphone.EditPhoneActivity;
import com.sage.cabapp.ui.editphone.UpdatePhoneActivity;
import com.sage.cabapp.ui.freerides.FreeRidesActivity;
import com.sage.cabapp.ui.help.GeneralInquiryActivity;
import com.sage.cabapp.ui.help.HelpActivity;
import com.sage.cabapp.ui.howtopay.HowToPayActivity;
import com.sage.cabapp.ui.locationonmap.LocationOnMapActivity;
import com.sage.cabapp.ui.main.HomeActivity;
import com.sage.cabapp.ui.payment.PaymentActivity;
import com.sage.cabapp.ui.paymentprofile.ChangePaymentProfileActivity;
import com.sage.cabapp.ui.personalprofile.PersonalProfileActivity;
import com.sage.cabapp.ui.phoneverification.PhoneVerificationActivity;
import com.sage.cabapp.ui.promocode.PromoCodeActivity;
import com.sage.cabapp.ui.requestaccepted.RequestAcceptedActivity;
import com.sage.cabapp.ui.requestforcar.RequestForCarActivity;
import com.sage.cabapp.ui.savedplaces.SavedPlacesActivity;
import com.sage.cabapp.ui.setaddress.SetSourceAddressActivity;
import com.sage.cabapp.ui.setaddressconfirmpickup.SetAddressPickupActivity;
import com.sage.cabapp.ui.splash.SplashActivity;
import com.sage.cabapp.ui.tipthemmore.TipThemMoreActivity;
import com.sage.cabapp.ui.tripfare.TripFareActivity;
import com.sage.cabapp.ui.triphistory.TripHistoryActivity;
import com.sage.cabapp.ui.tripinquirypage.TripInquiryActivity;
import com.sage.cabapp.ui.tripreceipt.TripReceiptActivity;
import com.sage.cabapp.ui.tutorial.TutorialActivity;
import com.sage.cabapp.ui.updateroute.UpdateRouteActivity;
import com.sage.cabapp.ui.verifyotp.VerifyOTPActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Maroof Ahmed Siddique on 14-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract SplashActivity bindSplashActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract PhoneVerificationActivity bindPhoneVerificationActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract TutorialActivity bindTutorialActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract VerifyOTPActivity bindVerifyOTPActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract AddEmailSignupActivity bindAddEmailSignupActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract AddNameSignupActivity bindAddNameSignupActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract HowToPayActivity bindHowToPayActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract HomeActivity bindHomeActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract SetSourceAddressActivity bindSetAddressActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract LocationOnMapActivity bindLocationOnMapActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract RequestForCarActivity bindRequestForCarActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract ConfirmPickupMapActivity bindConfirmPickupMapActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract SetAddressPickupActivity bindSetAddressPickupActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract RequestAcceptedActivity bindRequestAcceptedActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract AddDriverRatingActivity bindAddDriverRatingActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract AddDriverTipActivity bindAddDriverTipActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract TripHistoryActivity bindTripHistoryActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract TipThemMoreActivity bindTipThemMoreActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract AccountSettingsActivity bindAccountSettingsActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract EditNameActivity bindEditNameActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract EditPhoneActivity bindEditPhoneActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract EditEmailActivity bindEditEmailActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract UpdatePhoneActivity bindUpdatePhoneActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract SavedPlacesActivity bindSavedPlacesActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract PromoCodeActivity bindPromoCodeActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract AddPromoCodeActivity bindAddPromoCodeActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract AddHomePlaceActivity bindAddHomePlaceActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract AddWorkPlaceActivity bindAddWorkPlaceActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract ChangePaymentProfileActivity bindChangePaymentProfileActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract AddBusinessProfileActivity bindAddBusinessProfileActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract FreeRidesActivity bindFreeRidesActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract TripFareActivity bindTripFareActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract TripReceiptActivity bindTripReceiptActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract TripInquiryActivity bindtripInquiryActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract DriverProfileActivity bindDriverProfileActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract HelpActivity bindHelpActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract GeneralInquiryActivity bindGeneralInquiryActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract NewBusinessProfileActivity bindNewBusinessProfileActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract PaymentActivity bindPaymentActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract AddPaymentMethodActivity bindAddPaymentMethodActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract PersonalProfileActivity bindPersonalProfileActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract EditBusinessProfileActivity bindEditBusinessProfileActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract EditCardActivity bindEditCardActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract ChatModuleNewActivity bindChatModuleNewActivity();

    @ContributesAndroidInjector(modules = AppModule.class)
    abstract UpdateRouteActivity bindUpdateRouteActivity();
}