(function ($, undefined) {

    $.util.namespace("$.easyui.icons");

    var iconData = [
        { iconCls: "icon-cologne-address", cls: ".icon-cologne-address", text: "icon-cologne-address", path: "icon-cologne/16x16/address.png" },
        { iconCls: "icon-cologne-administrative-docs", cls: ".icon-cologne-administrative-docs", text: "icon-cologne-administrative-docs", path: "icon-cologne/16x16/administrative-docs.png" },
        { iconCls: "icon-cologne-advertising", cls: ".icon-cologne-advertising", text: "icon-cologne-advertising", path: "icon-cologne/16x16/advertising.png" },
        { iconCls: "icon-cologne-archives", cls: ".icon-cologne-archives", text: "icon-cologne-archives", path: "icon-cologne/16x16/archives.png" },
        { iconCls: "icon-cologne-attibutes", cls: ".icon-cologne-attibutes", text: "icon-cologne-attibutes", path: "icon-cologne/16x16/attibutes.png" },
        { iconCls: "icon-cologne-bank", cls: ".icon-cologne-bank", text: "icon-cologne-bank", path: "icon-cologne/16x16/bank.png" },
        { iconCls: "icon-cologne-basket", cls: ".icon-cologne-basket", text: "icon-cologne-basket", path: "icon-cologne/16x16/basket.png" },
        { iconCls: "icon-cologne-bestseller", cls: ".icon-cologne-bestseller", text: "icon-cologne-bestseller", path: "icon-cologne/16x16/bestseller.png" },
        { iconCls: "icon-cologne-billing", cls: ".icon-cologne-billing", text: "icon-cologne-billing", path: "icon-cologne/16x16/billing.png" },
        { iconCls: "icon-cologne-bookmark", cls: ".icon-cologne-bookmark", text: "icon-cologne-bookmark", path: "icon-cologne/16x16/bookmark.png" },
        { iconCls: "icon-cologne-brainstorming", cls: ".icon-cologne-brainstorming", text: "icon-cologne-brainstorming", path: "icon-cologne/16x16/brainstorming.png" },
        { iconCls: "icon-cologne-business-contact", cls: ".icon-cologne-business-contact", text: "icon-cologne-business-contact", path: "icon-cologne/16x16/business-contact.png" },
        { iconCls: "icon-cologne-busy", cls: ".icon-cologne-busy", text: "icon-cologne-busy", path: "icon-cologne/16x16/busy.png" },
        { iconCls: "icon-cologne-calendar", cls: ".icon-cologne-calendar", text: "icon-cologne-calendar", path: "icon-cologne/16x16/calendar.png" },
        { iconCls: "icon-cologne-category", cls: ".icon-cologne-category", text: "icon-cologne-category", path: "icon-cologne/16x16/category.png" },
        { iconCls: "icon-cologne-check", cls: ".icon-cologne-check", text: "icon-cologne-check", path: "icon-cologne/16x16/check.png" },
        { iconCls: "icon-cologne-collaboration", cls: ".icon-cologne-collaboration", text: "icon-cologne-collaboration", path: "icon-cologne/16x16/collaboration.png" },
        { iconCls: "icon-cologne-comment", cls: ".icon-cologne-comment", text: "icon-cologne-comment", path: "icon-cologne/16x16/comment.png" },
        { iconCls: "icon-cologne-communication", cls: ".icon-cologne-communication", text: "icon-cologne-communication", path: "icon-cologne/16x16/communication.png" },
        { iconCls: "icon-cologne-config", cls: ".icon-cologne-config", text: "icon-cologne-config", path: "icon-cologne/16x16/config.png" },
        { iconCls: "icon-cologne-consulting", cls: ".icon-cologne-consulting", text: "icon-cologne-consulting", path: "icon-cologne/16x16/consulting.png" },
        { iconCls: "icon-cologne-contact", cls: ".icon-cologne-contact", text: "icon-cologne-contact", path: "icon-cologne/16x16/contact.png" },
        { iconCls: "icon-cologne-cost", cls: ".icon-cologne-cost", text: "icon-cologne-cost", path: "icon-cologne/16x16/cost.png" },
        { iconCls: "icon-cologne-credit-card", cls: ".icon-cologne-credit-card", text: "icon-cologne-credit-card", path: "icon-cologne/16x16/credit-card.png" },
        { iconCls: "icon-cologne-credit", cls: ".icon-cologne-credit", text: "icon-cologne-credit", path: "icon-cologne/16x16/credit.png" },
        { iconCls: "icon-cologne-current-work", cls: ".icon-cologne-current-work", text: "icon-cologne-current-work", path: "icon-cologne/16x16/current-work.png" },
        { iconCls: "icon-cologne-customers", cls: ".icon-cologne-customers", text: "icon-cologne-customers", path: "icon-cologne/16x16/customers.png" },
        { iconCls: "icon-cologne-cv", cls: ".icon-cologne-cv", text: "icon-cologne-cv", path: "icon-cologne/16x16/cv.png" },
        { iconCls: "icon-cologne-database", cls: ".icon-cologne-database", text: "icon-cologne-database", path: "icon-cologne/16x16/database.png" },
        { iconCls: "icon-cologne-date", cls: ".icon-cologne-date", text: "icon-cologne-date", path: "icon-cologne/16x16/date.png" },
        { iconCls: "icon-cologne-delicious", cls: ".icon-cologne-delicious", text: "icon-cologne-delicious", path: "icon-cologne/16x16/delicious.png" },
        { iconCls: "icon-cologne-document-library", cls: ".icon-cologne-document-library", text: "icon-cologne-document-library", path: "icon-cologne/16x16/document-library.png" },
        { iconCls: "icon-cologne-donate", cls: ".icon-cologne-donate", text: "icon-cologne-donate", path: "icon-cologne/16x16/donate.png" },
        { iconCls: "icon-cologne-drawings", cls: ".icon-cologne-drawings", text: "icon-cologne-drawings", path: "icon-cologne/16x16/drawings.png" },
        { iconCls: "icon-cologne-edit", cls: ".icon-cologne-edit", text: "icon-cologne-edit", path: "icon-cologne/16x16/edit.png" },
        { iconCls: "icon-cologne-email", cls: ".icon-cologne-email", text: "icon-cologne-email", path: "icon-cologne/16x16/email.png" },
        { iconCls: "icon-cologne-featured", cls: ".icon-cologne-featured", text: "icon-cologne-featured", path: "icon-cologne/16x16/featured.png" },
        { iconCls: "icon-cologne-feed", cls: ".icon-cologne-feed", text: "icon-cologne-feed", path: "icon-cologne/16x16/feed.png" },
        { iconCls: "icon-cologne-finished-work", cls: ".icon-cologne-finished-work", text: "icon-cologne-finished-work", path: "icon-cologne/16x16/finished-work.png" },
        { iconCls: "icon-cologne-flag", cls: ".icon-cologne-flag", text: "icon-cologne-flag", path: "icon-cologne/16x16/flag.png" },
        { iconCls: "icon-cologne-folder", cls: ".icon-cologne-folder", text: "icon-cologne-folder", path: "icon-cologne/16x16/folder.png" },
        { iconCls: "icon-cologne-free-for-job", cls: ".icon-cologne-free-for-job", text: "icon-cologne-free-for-job", path: "icon-cologne/16x16/free-for-job.png" },
        { iconCls: "icon-cologne-freelance", cls: ".icon-cologne-freelance", text: "icon-cologne-freelance", path: "icon-cologne/16x16/freelance.png" },
        { iconCls: "icon-cologne-full-time", cls: ".icon-cologne-full-time", text: "icon-cologne-full-time", path: "icon-cologne/16x16/full-time.png" },
        { iconCls: "icon-cologne-future-projects", cls: ".icon-cologne-future-projects", text: "icon-cologne-future-projects", path: "icon-cologne/16x16/future-projects.png" },
        { iconCls: "icon-cologne-graphic-design", cls: ".icon-cologne-graphic-design", text: "icon-cologne-graphic-design", path: "icon-cologne/16x16/graphic-design.png" },
        { iconCls: "icon-cologne-heart", cls: ".icon-cologne-heart", text: "icon-cologne-heart", path: "icon-cologne/16x16/heart.png" },
        { iconCls: "icon-cologne-hire-me", cls: ".icon-cologne-hire-me", text: "icon-cologne-hire-me", path: "icon-cologne/16x16/hire-me.png" },
        { iconCls: "icon-cologne-home", cls: ".icon-cologne-home", text: "icon-cologne-home", path: "icon-cologne/16x16/home.png" },
        { iconCls: "icon-cologne-illustration", cls: ".icon-cologne-illustration", text: "icon-cologne-illustration", path: "icon-cologne/16x16/illustration.png" },
        { iconCls: "icon-cologne-invoice", cls: ".icon-cologne-invoice", text: "icon-cologne-invoice", path: "icon-cologne/16x16/invoice.png" },
        { iconCls: "icon-cologne-issue", cls: ".icon-cologne-issue", text: "icon-cologne-issue", path: "icon-cologne/16x16/issue.png" },
        { iconCls: "icon-cologne-library", cls: ".icon-cologne-library", text: "icon-cologne-library", path: "icon-cologne/16x16/library.png" },
        { iconCls: "icon-cologne-lightbulb", cls: ".icon-cologne-lightbulb", text: "icon-cologne-lightbulb", path: "icon-cologne/16x16/lightbulb.png" },
        { iconCls: "icon-cologne-limited-edition", cls: ".icon-cologne-limited-edition", text: "icon-cologne-limited-edition", path: "icon-cologne/16x16/limited-edition.png" },
        { iconCls: "icon-cologne-link", cls: ".icon-cologne-link", text: "icon-cologne-link", path: "icon-cologne/16x16/link.png" },
        { iconCls: "icon-cologne-lock", cls: ".icon-cologne-lock", text: "icon-cologne-lock", path: "icon-cologne/16x16/lock.png" },
        { iconCls: "icon-cologne-login", cls: ".icon-cologne-login", text: "icon-cologne-login", path: "icon-cologne/16x16/login.png" },
        { iconCls: "icon-cologne-logout", cls: ".icon-cologne-logout", text: "icon-cologne-logout", path: "icon-cologne/16x16/logout.png" },
        { iconCls: "icon-cologne-milestone", cls: ".icon-cologne-milestone", text: "icon-cologne-milestone", path: "icon-cologne/16x16/milestone.png" },
        { iconCls: "icon-cologne-my-account", cls: ".icon-cologne-my-account", text: "icon-cologne-my-account", path: "icon-cologne/16x16/my-account.png" },
        { iconCls: "icon-cologne-networking", cls: ".icon-cologne-networking", text: "icon-cologne-networking", path: "icon-cologne/16x16/networking.png" },
        { iconCls: "icon-cologne-old-versions", cls: ".icon-cologne-old-versions", text: "icon-cologne-old-versions", path: "icon-cologne/16x16/old-versions.png" },
        { iconCls: "icon-cologne-order-1", cls: ".icon-cologne-order-1", text: "icon-cologne-order-1", path: "icon-cologne/16x16/order-1.png" },
        { iconCls: "icon-cologne-order", cls: ".icon-cologne-order", text: "icon-cologne-order", path: "icon-cologne/16x16/order.png" },
        { iconCls: "icon-cologne-payment-card", cls: ".icon-cologne-payment-card", text: "icon-cologne-payment-card", path: "icon-cologne/16x16/payment-card.png" },
        { iconCls: "icon-cologne-paypal", cls: ".icon-cologne-paypal", text: "icon-cologne-paypal", path: "icon-cologne/16x16/paypal.png" },
        { iconCls: "icon-cologne-pen", cls: ".icon-cologne-pen", text: "icon-cologne-pen", path: "icon-cologne/16x16/pen.png" },
        { iconCls: "icon-cologne-pencil", cls: ".icon-cologne-pencil", text: "icon-cologne-pencil", path: "icon-cologne/16x16/pencil.png" },
        { iconCls: "icon-cologne-phone", cls: ".icon-cologne-phone", text: "icon-cologne-phone", path: "icon-cologne/16x16/phone.png" },
        { iconCls: "icon-cologne-photography", cls: ".icon-cologne-photography", text: "icon-cologne-photography", path: "icon-cologne/16x16/photography.png" },
        { iconCls: "icon-cologne-plus", cls: ".icon-cologne-plus", text: "icon-cologne-plus", path: "icon-cologne/16x16/plus.png" },
        { iconCls: "icon-cologne-premium", cls: ".icon-cologne-premium", text: "icon-cologne-premium", path: "icon-cologne/16x16/premium.png" },
        { iconCls: "icon-cologne-print", cls: ".icon-cologne-print", text: "icon-cologne-print", path: "icon-cologne/16x16/print.png" },
        { iconCls: "icon-cologne-process", cls: ".icon-cologne-process", text: "icon-cologne-process", path: "icon-cologne/16x16/process.png" },
        { iconCls: "icon-cologne-product-1", cls: ".icon-cologne-product-1", text: "icon-cologne-product-1", path: "icon-cologne/16x16/product-1.png" },
        { iconCls: "icon-cologne-product-design", cls: ".icon-cologne-product-design", text: "icon-cologne-product-design", path: "icon-cologne/16x16/product-design.png" },
        { iconCls: "icon-cologne-product", cls: ".icon-cologne-product", text: "icon-cologne-product", path: "icon-cologne/16x16/product.png" },
        { iconCls: "icon-cologne-project", cls: ".icon-cologne-project", text: "icon-cologne-project", path: "icon-cologne/16x16/project.png" },
        { iconCls: "icon-cologne-publish", cls: ".icon-cologne-publish", text: "icon-cologne-publish", path: "icon-cologne/16x16/publish.png" },
        { iconCls: "icon-cologne-refresh", cls: ".icon-cologne-refresh", text: "icon-cologne-refresh", path: "icon-cologne/16x16/refresh.png" },
        { iconCls: "icon-cologne-search", cls: ".icon-cologne-search", text: "icon-cologne-search", path: "icon-cologne/16x16/search.png" },
        { iconCls: "icon-cologne-settings", cls: ".icon-cologne-settings", text: "icon-cologne-settings", path: "icon-cologne/16x16/settings.png" },
        { iconCls: "icon-cologne-shipping", cls: ".icon-cologne-shipping", text: "icon-cologne-shipping", path: "icon-cologne/16x16/shipping.png" },
        { iconCls: "icon-cologne-showreel", cls: ".icon-cologne-showreel", text: "icon-cologne-showreel", path: "icon-cologne/16x16/showreel.png" },
        { iconCls: "icon-cologne-sign-in", cls: ".icon-cologne-sign-in", text: "icon-cologne-sign-in", path: "icon-cologne/16x16/sign-in.png" },
        { iconCls: "icon-cologne-sign-out", cls: ".icon-cologne-sign-out", text: "icon-cologne-sign-out", path: "icon-cologne/16x16/sign-out.png" },
        { iconCls: "icon-cologne-sign-up", cls: ".icon-cologne-sign-up", text: "icon-cologne-sign-up", path: "icon-cologne/16x16/sign-up.png" },
        { iconCls: "icon-cologne-sitemap", cls: ".icon-cologne-sitemap", text: "icon-cologne-sitemap", path: "icon-cologne/16x16/sitemap.png" },
        { iconCls: "icon-cologne-special-offer", cls: ".icon-cologne-special-offer", text: "icon-cologne-special-offer", path: "icon-cologne/16x16/special-offer.png" },
        { iconCls: "icon-cologne-star", cls: ".icon-cologne-star", text: "icon-cologne-star", path: "icon-cologne/16x16/star.png" },
        { iconCls: "icon-cologne-statistics", cls: ".icon-cologne-statistics", text: "icon-cologne-statistics", path: "icon-cologne/16x16/statistics.png" },
        { iconCls: "icon-cologne-suppliers", cls: ".icon-cologne-suppliers", text: "icon-cologne-suppliers", path: "icon-cologne/16x16/suppliers.png" },
        { iconCls: "icon-cologne-tag", cls: ".icon-cologne-tag", text: "icon-cologne-tag", path: "icon-cologne/16x16/tag.png" },
        { iconCls: "icon-cologne-ticket", cls: ".icon-cologne-ticket", text: "icon-cologne-ticket", path: "icon-cologne/16x16/ticket.png" },
        { iconCls: "icon-cologne-twitter", cls: ".icon-cologne-twitter", text: "icon-cologne-twitter", path: "icon-cologne/16x16/twitter.png" },
        { iconCls: "icon-cologne-upcoming-work", cls: ".icon-cologne-upcoming-work", text: "icon-cologne-upcoming-work", path: "icon-cologne/16x16/upcoming-work.png" },
        { iconCls: "icon-cologne-user", cls: ".icon-cologne-user", text: "icon-cologne-user", path: "icon-cologne/16x16/user.png" },
        { iconCls: "icon-cologne-world", cls: ".icon-cologne-world", text: "icon-cologne-world", path: "icon-cologne/16x16/world.png" },
        { iconCls: "icon-cologne-zoom", cls: ".icon-cologne-zoom", text: "icon-cologne-zoom", path: "icon-cologne/16x16/zoom.png" },


        { iconCls: "icon-cologne-32-address", cls: ".icon-cologne-32-address", text: "icon-cologne-32-address", path: "icon-cologne/32x32/address.png" },
        { iconCls: "icon-cologne-32-administrative-docs", cls: ".icon-cologne-32-administrative-docs", text: "icon-cologne-32-administrative-docs", path: "icon-cologne/32x32/administrative-docs.png" },
        { iconCls: "icon-cologne-32-advertising", cls: ".icon-cologne-32-advertising", text: "icon-cologne-32-advertising", path: "icon-cologne/32x32/advertising.png" },
        { iconCls: "icon-cologne-32-archives", cls: ".icon-cologne-32-archives", text: "icon-cologne-32-archives", path: "icon-cologne/32x32/archives.png" },
        { iconCls: "icon-cologne-32-attibutes", cls: ".icon-cologne-32-attibutes", text: "icon-cologne-32-attibutes", path: "icon-cologne/32x32/attibutes.png" },
        { iconCls: "icon-cologne-32-bank", cls: ".icon-cologne-32-bank", text: "icon-cologne-32-bank", path: "icon-cologne/32x32/bank.png" },
        { iconCls: "icon-cologne-32-basket", cls: ".icon-cologne-32-basket", text: "icon-cologne-32-basket", path: "icon-cologne/32x32/basket.png" },
        { iconCls: "icon-cologne-32-bestseller", cls: ".icon-cologne-32-bestseller", text: "icon-cologne-32-bestseller", path: "icon-cologne/32x32/bestseller.png" },
        { iconCls: "icon-cologne-32-billing", cls: ".icon-cologne-32-billing", text: "icon-cologne-32-billing", path: "icon-cologne/32x32/billing.png" },
        { iconCls: "icon-cologne-32-bookmark", cls: ".icon-cologne-32-bookmark", text: "icon-cologne-32-bookmark", path: "icon-cologne/32x32/bookmark.png" },
        { iconCls: "icon-cologne-32-brainstorming", cls: ".icon-cologne-32-brainstorming", text: "icon-cologne-32-brainstorming", path: "icon-cologne/32x32/brainstorming.png" },
        { iconCls: "icon-cologne-32-business-contact", cls: ".icon-cologne-32-business-contact", text: "icon-cologne-32-business-contact", path: "icon-cologne/32x32/business-contact.png" },
        { iconCls: "icon-cologne-32-busy", cls: ".icon-cologne-32-busy", text: "icon-cologne-32-busy", path: "icon-cologne/32x32/busy.png" },
        { iconCls: "icon-cologne-32-calendar", cls: ".icon-cologne-32-calendar", text: "icon-cologne-32-calendar", path: "icon-cologne/32x32/calendar.png" },
        { iconCls: "icon-cologne-32-category", cls: ".icon-cologne-32-category", text: "icon-cologne-32-category", path: "icon-cologne/32x32/category.png" },
        { iconCls: "icon-cologne-32-check", cls: ".icon-cologne-32-check", text: "icon-cologne-32-check", path: "icon-cologne/32x32/check.png" },
        { iconCls: "icon-cologne-32-collaboration", cls: ".icon-cologne-32-collaboration", text: "icon-cologne-32-collaboration", path: "icon-cologne/32x32/collaboration.png" },
        { iconCls: "icon-cologne-32-comment", cls: ".icon-cologne-32-comment", text: "icon-cologne-32-comment", path: "icon-cologne/32x32/comment.png" },
        { iconCls: "icon-cologne-32-communication", cls: ".icon-cologne-32-communication", text: "icon-cologne-32-communication", path: "icon-cologne/32x32/communication.png" },
        { iconCls: "icon-cologne-32-config", cls: ".icon-cologne-32-config", text: "icon-cologne-32-config", path: "icon-cologne/32x32/config.png" },
        { iconCls: "icon-cologne-32-consulting", cls: ".icon-cologne-32-consulting", text: "icon-cologne-32-consulting", path: "icon-cologne/32x32/consulting.png" },
        { iconCls: "icon-cologne-32-contact", cls: ".icon-cologne-32-contact", text: "icon-cologne-32-contact", path: "icon-cologne/32x32/contact.png" },
        { iconCls: "icon-cologne-32-cost", cls: ".icon-cologne-32-cost", text: "icon-cologne-32-cost", path: "icon-cologne/32x32/cost.png" },
        { iconCls: "icon-cologne-32-credit-card", cls: ".icon-cologne-32-credit-card", text: "icon-cologne-32-credit-card", path: "icon-cologne/32x32/credit-card.png" },
        { iconCls: "icon-cologne-32-credit", cls: ".icon-cologne-32-credit", text: "icon-cologne-32-credit", path: "icon-cologne/32x32/credit.png" },
        { iconCls: "icon-cologne-32-current-work", cls: ".icon-cologne-32-current-work", text: "icon-cologne-32-current-work", path: "icon-cologne/32x32/current-work.png" },
        { iconCls: "icon-cologne-32-customers", cls: ".icon-cologne-32-customers", text: "icon-cologne-32-customers", path: "icon-cologne/32x32/customers.png" },
        { iconCls: "icon-cologne-32-cv", cls: ".icon-cologne-32-cv", text: "icon-cologne-32-cv", path: "icon-cologne/32x32/cv.png" },
        { iconCls: "icon-cologne-32-database", cls: ".icon-cologne-32-database", text: "icon-cologne-32-database", path: "icon-cologne/32x32/database.png" },
        { iconCls: "icon-cologne-32-date", cls: ".icon-cologne-32-date", text: "icon-cologne-32-date", path: "icon-cologne/32x32/date.png" },
        { iconCls: "icon-cologne-32-delicious", cls: ".icon-cologne-32-delicious", text: "icon-cologne-32-delicious", path: "icon-cologne/32x32/delicious.png" },
        { iconCls: "icon-cologne-32-document-library", cls: ".icon-cologne-32-document-library", text: "icon-cologne-32-document-library", path: "icon-cologne/32x32/document-library.png" },
        { iconCls: "icon-cologne-32-donate", cls: ".icon-cologne-32-donate", text: "icon-cologne-32-donate", path: "icon-cologne/32x32/donate.png" },
        { iconCls: "icon-cologne-32-drawings", cls: ".icon-cologne-32-drawings", text: "icon-cologne-32-drawings", path: "icon-cologne/32x32/drawings.png" },
        { iconCls: "icon-cologne-32-edit", cls: ".icon-cologne-32-edit", text: "icon-cologne-32-edit", path: "icon-cologne/32x32/edit.png" },
        { iconCls: "icon-cologne-32-email", cls: ".icon-cologne-32-email", text: "icon-cologne-32-email", path: "icon-cologne/32x32/email.png" },
        { iconCls: "icon-cologne-32-featured", cls: ".icon-cologne-32-featured", text: "icon-cologne-32-featured", path: "icon-cologne/32x32/featured.png" },
        { iconCls: "icon-cologne-32-feed", cls: ".icon-cologne-32-feed", text: "icon-cologne-32-feed", path: "icon-cologne/32x32/feed.png" },
        { iconCls: "icon-cologne-32-finished-work", cls: ".icon-cologne-32-finished-work", text: "icon-cologne-32-finished-work", path: "icon-cologne/32x32/finished-work.png" },
        { iconCls: "icon-cologne-32-flag", cls: ".icon-cologne-32-flag", text: "icon-cologne-32-flag", path: "icon-cologne/32x32/flag.png" },
        { iconCls: "icon-cologne-32-folder", cls: ".icon-cologne-32-folder", text: "icon-cologne-32-folder", path: "icon-cologne/32x32/folder.png" },
        { iconCls: "icon-cologne-32-free-for-job", cls: ".icon-cologne-32-free-for-job", text: "icon-cologne-32-free-for-job", path: "icon-cologne/32x32/free-for-job.png" },
        { iconCls: "icon-cologne-32-freelance", cls: ".icon-cologne-32-freelance", text: "icon-cologne-32-freelance", path: "icon-cologne/32x32/freelance.png" },
        { iconCls: "icon-cologne-32-full-time", cls: ".icon-cologne-32-full-time", text: "icon-cologne-32-full-time", path: "icon-cologne/32x32/full-time.png" },
        { iconCls: "icon-cologne-32-future-projects", cls: ".icon-cologne-32-future-projects", text: "icon-cologne-32-future-projects", path: "icon-cologne/32x32/future-projects.png" },
        { iconCls: "icon-cologne-32-graphic-design", cls: ".icon-cologne-32-graphic-design", text: "icon-cologne-32-graphic-design", path: "icon-cologne/32x32/graphic-design.png" },
        { iconCls: "icon-cologne-32-heart", cls: ".icon-cologne-32-heart", text: "icon-cologne-32-heart", path: "icon-cologne/32x32/heart.png" },
        { iconCls: "icon-cologne-32-hire-me", cls: ".icon-cologne-32-hire-me", text: "icon-cologne-32-hire-me", path: "icon-cologne/32x32/hire-me.png" },
        { iconCls: "icon-cologne-32-home", cls: ".icon-cologne-32-home", text: "icon-cologne-32-home", path: "icon-cologne/32x32/home.png" },
        { iconCls: "icon-cologne-32-illustration", cls: ".icon-cologne-32-illustration", text: "icon-cologne-32-illustration", path: "icon-cologne/32x32/illustration.png" },
        { iconCls: "icon-cologne-32-invoice", cls: ".icon-cologne-32-invoice", text: "icon-cologne-32-invoice", path: "icon-cologne/32x32/invoice.png" },
        { iconCls: "icon-cologne-32-issue", cls: ".icon-cologne-32-issue", text: "icon-cologne-32-issue", path: "icon-cologne/32x32/issue.png" },
        { iconCls: "icon-cologne-32-library", cls: ".icon-cologne-32-library", text: "icon-cologne-32-library", path: "icon-cologne/32x32/library.png" },
        { iconCls: "icon-cologne-32-lightbulb", cls: ".icon-cologne-32-lightbulb", text: "icon-cologne-32-lightbulb", path: "icon-cologne/32x32/lightbulb.png" },
        { iconCls: "icon-cologne-32-limited-edition", cls: ".icon-cologne-32-limited-edition", text: "icon-cologne-32-limited-edition", path: "icon-cologne/32x32/limited-edition.png" },
        { iconCls: "icon-cologne-32-link", cls: ".icon-cologne-32-link", text: "icon-cologne-32-link", path: "icon-cologne/32x32/link.png" },
        { iconCls: "icon-cologne-32-lock", cls: ".icon-cologne-32-lock", text: "icon-cologne-32-lock", path: "icon-cologne/32x32/lock.png" },
        { iconCls: "icon-cologne-32-login", cls: ".icon-cologne-32-login", text: "icon-cologne-32-login", path: "icon-cologne/32x32/login.png" },
        { iconCls: "icon-cologne-32-logout", cls: ".icon-cologne-32-logout", text: "icon-cologne-32-logout", path: "icon-cologne/32x32/logout.png" },
        { iconCls: "icon-cologne-32-milestone", cls: ".icon-cologne-32-milestone", text: "icon-cologne-32-milestone", path: "icon-cologne/32x32/milestone.png" },
        { iconCls: "icon-cologne-32-my-account", cls: ".icon-cologne-32-my-account", text: "icon-cologne-32-my-account", path: "icon-cologne/32x32/my-account.png" },
        { iconCls: "icon-cologne-32-networking", cls: ".icon-cologne-32-networking", text: "icon-cologne-32-networking", path: "icon-cologne/32x32/networking.png" },
        { iconCls: "icon-cologne-32-old-versions", cls: ".icon-cologne-32-old-versions", text: "icon-cologne-32-old-versions", path: "icon-cologne/32x32/old-versions.png" },
        { iconCls: "icon-cologne-32-order-1", cls: ".icon-cologne-32-order-1", text: "icon-cologne-32-order-1", path: "icon-cologne/32x32/order-1.png" },
        { iconCls: "icon-cologne-32-order", cls: ".icon-cologne-32-order", text: "icon-cologne-32-order", path: "icon-cologne/32x32/order.png" },
        { iconCls: "icon-cologne-32-payment-card", cls: ".icon-cologne-32-payment-card", text: "icon-cologne-32-payment-card", path: "icon-cologne/32x32/payment-card.png" },
        { iconCls: "icon-cologne-32-paypal", cls: ".icon-cologne-32-paypal", text: "icon-cologne-32-paypal", path: "icon-cologne/32x32/paypal.png" },
        { iconCls: "icon-cologne-32-pen", cls: ".icon-cologne-32-pen", text: "icon-cologne-32-pen", path: "icon-cologne/32x32/pen.png" },
        { iconCls: "icon-cologne-32-pencil", cls: ".icon-cologne-32-pencil", text: "icon-cologne-32-pencil", path: "icon-cologne/32x32/pencil.png" },
        { iconCls: "icon-cologne-32-phone", cls: ".icon-cologne-32-phone", text: "icon-cologne-32-phone", path: "icon-cologne/32x32/phone.png" },
        { iconCls: "icon-cologne-32-photography", cls: ".icon-cologne-32-photography", text: "icon-cologne-32-photography", path: "icon-cologne/32x32/photography.png" },
        { iconCls: "icon-cologne-32-plus", cls: ".icon-cologne-32-plus", text: "icon-cologne-32-plus", path: "icon-cologne/32x32/plus.png" },
        { iconCls: "icon-cologne-32-premium", cls: ".icon-cologne-32-premium", text: "icon-cologne-32-premium", path: "icon-cologne/32x32/premium.png" },
        { iconCls: "icon-cologne-32-print", cls: ".icon-cologne-32-print", text: "icon-cologne-32-print", path: "icon-cologne/32x32/print.png" },
        { iconCls: "icon-cologne-32-process", cls: ".icon-cologne-32-process", text: "icon-cologne-32-process", path: "icon-cologne/32x32/process.png" },
        { iconCls: "icon-cologne-32-product-1", cls: ".icon-cologne-32-product-1", text: "icon-cologne-32-product-1", path: "icon-cologne/32x32/product-1.png" },
        { iconCls: "icon-cologne-32-product-design", cls: ".icon-cologne-32-product-design", text: "icon-cologne-32-product-design", path: "icon-cologne/32x32/product-design.png" },
        { iconCls: "icon-cologne-32-product", cls: ".icon-cologne-32-product", text: "icon-cologne-32-product", path: "icon-cologne/32x32/product.png" },
        { iconCls: "icon-cologne-32-project", cls: ".icon-cologne-32-project", text: "icon-cologne-32-project", path: "icon-cologne/32x32/project.png" },
        { iconCls: "icon-cologne-32-publish", cls: ".icon-cologne-32-publish", text: "icon-cologne-32-publish", path: "icon-cologne/32x32/publish.png" },
        { iconCls: "icon-cologne-32-refresh", cls: ".icon-cologne-32-refresh", text: "icon-cologne-32-refresh", path: "icon-cologne/32x32/refresh.png" },
        { iconCls: "icon-cologne-32-search", cls: ".icon-cologne-32-search", text: "icon-cologne-32-search", path: "icon-cologne/32x32/search.png" },
        { iconCls: "icon-cologne-32-settings", cls: ".icon-cologne-32-settings", text: "icon-cologne-32-settings", path: "icon-cologne/32x32/settings.png" },
        { iconCls: "icon-cologne-32-shipping", cls: ".icon-cologne-32-shipping", text: "icon-cologne-32-shipping", path: "icon-cologne/32x32/shipping.png" },
        { iconCls: "icon-cologne-32-showreel", cls: ".icon-cologne-32-showreel", text: "icon-cologne-32-showreel", path: "icon-cologne/32x32/showreel.png" },
        { iconCls: "icon-cologne-32-sign-in", cls: ".icon-cologne-32-sign-in", text: "icon-cologne-32-sign-in", path: "icon-cologne/32x32/sign-in.png" },
        { iconCls: "icon-cologne-32-sign-out", cls: ".icon-cologne-32-sign-out", text: "icon-cologne-32-sign-out", path: "icon-cologne/32x32/sign-out.png" },
        { iconCls: "icon-cologne-32-sign-up", cls: ".icon-cologne-32-sign-up", text: "icon-cologne-32-sign-up", path: "icon-cologne/32x32/sign-up.png" },
        { iconCls: "icon-cologne-32-sitemap", cls: ".icon-cologne-32-sitemap", text: "icon-cologne-32-sitemap", path: "icon-cologne/32x32/sitemap.png" },
        { iconCls: "icon-cologne-32-special-offer", cls: ".icon-cologne-32-special-offer", text: "icon-cologne-32-special-offer", path: "icon-cologne/32x32/special-offer.png" },
        { iconCls: "icon-cologne-32-star", cls: ".icon-cologne-32-star", text: "icon-cologne-32-star", path: "icon-cologne/32x32/star.png" },
        { iconCls: "icon-cologne-32-statistics", cls: ".icon-cologne-32-statistics", text: "icon-cologne-32-statistics", path: "icon-cologne/32x32/statistics.png" },
        { iconCls: "icon-cologne-32-suppliers", cls: ".icon-cologne-32-suppliers", text: "icon-cologne-32-suppliers", path: "icon-cologne/32x32/suppliers.png" },
        { iconCls: "icon-cologne-32-tag", cls: ".icon-cologne-32-tag", text: "icon-cologne-32-tag", path: "icon-cologne/32x32/tag.png" },
        { iconCls: "icon-cologne-32-ticket", cls: ".icon-cologne-32-ticket", text: "icon-cologne-32-ticket", path: "icon-cologne/32x32/ticket.png" },
        { iconCls: "icon-cologne-32-twitter", cls: ".icon-cologne-32-twitter", text: "icon-cologne-32-twitter", path: "icon-cologne/32x32/twitter.png" },
        { iconCls: "icon-cologne-32-upcoming-work", cls: ".icon-cologne-32-upcoming-work", text: "icon-cologne-32-upcoming-work", path: "icon-cologne/32x32/upcoming-work.png" },
        { iconCls: "icon-cologne-32-user", cls: ".icon-cologne-32-user", text: "icon-cologne-32-user", path: "icon-cologne/32x32/user.png" },
        { iconCls: "icon-cologne-32-world", cls: ".icon-cologne-32-world", text: "icon-cologne-32-world", path: "icon-cologne/32x32/world.png" },
        { iconCls: "icon-cologne-32-zoom", cls: ".icon-cologne-32-zoom", text: "icon-cologne-32-zoom", path: "icon-cologne/32x32/zoom.png" }
    ];

    $.easyui.icons.cologne = iconData;

    var iconStyle = { name: "cologne", size: "16,32" };
    if ($.isArray($.easyui.iconStyles)) { $.array.merge($.easyui.iconStyles, iconStyle); } else { $.easyui.iconStyles = [iconStyle]; }

})(jQuery);