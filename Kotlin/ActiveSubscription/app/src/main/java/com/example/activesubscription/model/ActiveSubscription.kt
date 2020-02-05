package com.example.activesubscription.model

/**
 * Created by Puneet Sharma on 11/10/2019
 */
data class CJRActiveSubscriptions(@SerializedName("head")
                                  var head: ActiveSubscriptionsHead? = null,
                                  @SerializedName("body") var body: ActiveSubscriptionsBody? = null ) : BaseDataModel()

class ActiveSubscriptionsHead( @SerializedName("version")
                               var version: String? = null,
                               @SerializedName("timestamp") var timestamp: String? = null) : BaseDataModel()

data class ActiveSubscriptionsBody(@SerializedName("subscriptionDetails")
                                   var subscriptionDetails: MutableList<ActiveSubscriptionDetailsItem>? = null,
                                   @SerializedName("resultInfo")
                                   var resultInfo: ActiveSubscriptionsResultInfo? = null) : BaseDataModel()

data class ActiveSubscriptionDetailsItem(@SerializedName("frequencyUnit")
                                         var frequencyUnit: String? = null,
                                         @SerializedName("subsCallbackUrl")
                                         var subsCallbackUrl: String? = null,
                                         @SerializedName("accountHolderName")
                                         var accountHolderName: String? = null,
                                         @SerializedName("merchantName")
                                         var merchantName: String? = null,
                                         @SerializedName("frequency") var frequency:Int = 0,
                                         @SerializedName("isCommunicationManager")
                                         var isCommunicationManager: String? = null,
                                         @SerializedName("subsHistoryDetails")
                                         var subsHistoryDetails: ActiveSubscriptionsHistoryDetails? = null,
                                         @SerializedName("merchantId")
                                         var merchantId: String? = null,
                                         @SerializedName("issuerBankAccNo")
                                         var issuerBankAccNo: String? = null,
                                         @SerializedName("isAutoRenewal")
                                         var isAutoRenewal: String? = null,
                                         @SerializedName("subscriptionStartDate")
                                         var subscriptionStartDate: String? = null,
                                         @SerializedName("paymentDetails")
                                         var paymentDetails: ActiveSubscriptionsPaymentDetails? = null,
                                         @SerializedName("customerDetails")
                                         var customerDetails: ActiveSubscriptionsCustomerDetails? = null,
                                         @SerializedName("subscriptionEndDate")
                                         var subscriptionEndDate: String? = null,
                                         @SerializedName("merchantLogo")
                                         var merchantLogo: String? = null,
                                         @SerializedName("subscriptionAmountType")
                                         var subscriptionAmountType: String? = null,
                                         @SerializedName("upfrontAmount")
                                         var upfrontAmount: String? = null,
                                         @SerializedName("isAutoRetry")
                                         var isAutoRetry: String? = null,
                                         @SerializedName("lastUpdatedDate")
                                         var lastUpdatedDate: String? = null,
                                         @SerializedName("onusMerchant")
                                         var isOnusMerchant:Boolean = false,
                                         @SerializedName("subscriptionType")
                                         var subscriptionType: String? = null,
                                         @SerializedName("subscriptionAmount")
                                         var subscriptionAmount: String? = null,
                                         @SerializedName("subscriptionId")
                                         var subscriptionId: String? = null,
                                         @SerializedName("issuerBankIfsc")
                                         var issuerBankIfsc: String? = null,
                                         @SerializedName("subsPaymentInstDetails")
                                         var subsPaymentInstDetails: ActiveSubscriptionsPaymentInstrumentDetails? = null,
                                         @SerializedName("renewalAmount")
                                         var renewalAmount: String? = null) : BaseDataModel()

data class ActiveSubscriptionsResultInfo(@SerializedName("code")
                                         var code: String? = null,
                                         @SerializedName("message")
                                         var message: String? = null,
                                         @SerializedName("status")
                                         var status: String? = null) : BaseDataModel()

data class ActiveSubscriptionsHistoryDetails(@SerializedName("totalSuccessfulRenewalAmount")
                                             var totalSuccessfulRenewalAmount: String? = null,
                                             @SerializedName("lastRenewalStatus")
                                             var lastRenewalStatus: String? = null,
                                             @SerializedName("totalSuccessfulRenewalOrders")
                                             var totalSuccessfulRenewalOrders: String? = null,
                                             @SerializedName("lastRenewaldate")
                                             var lastRenewaldate: String? = null,
                                             @SerializedName("totalRenewalOrders")
                                             var totalRenewalOrders: String? = null) : BaseDataModel()

data class ActiveSubscriptionsPaymentDetails(@SerializedName("lastSuccessOrderId")
                                             var lastSuccessOrderId: String? = null,
                                             @SerializedName("lastBilledAmount")
                                             var lastBilledAmount: String? = null,
                                             @SerializedName("lastSuccessTxnDate")
                                             var lastSuccessTxnDate: String? = null) : BaseDataModel()

data class ActiveSubscriptionsCustomerDetails(@SerializedName("customerEmail")
                                              var customerEmail: String? = null,
                                              @SerializedName("customerMobile")
                                              var customerMobile: String? = null,
                                              @SerializedName("customerName")
                                              var customerName: String? = null) : BaseDataModel()

data class ActiveSubscriptionsPaymentInstrumentDetails(@SerializedName("expiryDate")
                                                       var expiryDate: String? = null,
                                                       @SerializedName("instrumentStatus")
                                                       var instrumentStatus: String? = null,
                                                       @SerializedName("issuingBank")
                                                       var issuingBank: String? = null,
                                                       @SerializedName("issuingBankLogo")
                                                       var issuingBankLogo: String? = null,
                                                       @SerializedName("cardSchemeLogo")
                                                       var cardSchemeLogo: String? = null,
                                                       @SerializedName("cardScheme")
                                                       var cardScheme: String? = null,
                                                       @SerializedName("paymode")
                                                       var paymode: String? = null,
                                                       @SerializedName("maskedMobileNumber")
                                                       var maskedMobileNumber: String? = null,
                                                       @SerializedName("ifsc")
                                                       var ifsc: String? = null,
                                                       @SerializedName("maskedCardNumber")
                                                       var maskedCardNumber: String? = null,
                                                       @SerializedName("maskedAccountNumber")
                                                       var maskedAccountNumber: String? = null) : BaseDataModel()

