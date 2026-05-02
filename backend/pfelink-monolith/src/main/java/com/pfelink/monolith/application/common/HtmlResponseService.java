package com.pfelink.monolith.application.common;

import org.springframework.stereotype.Service;

@Service
public class HtmlResponseService {

    public String buildConfirmationPage(String action, String message, String postUrl, String color) {
        return """
            <html><head><title>Confirm %s - PFE-Link</title></head>
            <body style="font-family:Arial,sans-serif;display:flex;justify-content:center;align-items:center;min-height:100vh;margin:0;background:#f9fafb">
              <div style="text-align:center;padding:40px;background:#fff;border-radius:12px;box-shadow:0 4px 24px rgba(0,0,0,.08);max-width:400px">
                <h1 style="color:#111827;margin-bottom:16px">%s</h1>
                <p style="color:#666;font-size:16px;margin-bottom:32px">%s</p>
                <form action="%s" method="POST">
                  <button type="submit" style="background:%s;color:white;border:none;padding:12px 24px;border-radius:8px;font-size:16px;cursor:pointer;font-weight:600;width:100%%">
                    Confirm Action
                  </button>
                </form>
                <p style="color:#999;margin-top:24px;font-size:13px">PFE-Link Secure Action</p>
              </div>
            </body></html>
            """.formatted(action, action, message, postUrl, color);
    }

    public String buildPage(String title, String message, String color) {
        return """
            <html><head><title>%s - PFE-Link</title></head>
            <body style="font-family:Arial,sans-serif;display:flex;
              justify-content:center;align-items:center;min-height:100vh;
              margin:0;background:#f9fafb">
              <div style="text-align:center;padding:40px;background:#fff;
                border-radius:12px;box-shadow:0 4px 24px rgba(0,0,0,.08)">
                <h1 style="color:%s;margin-bottom:8px">%s</h1>
                <p style="color:#666;font-size:16px">%s</p>
                <p style="color:#999;margin-top:24px;font-size:13px">PFE-Link Platform</p>
              </div>
            </body></html>
            """.formatted(title, color, title, message);
    }
}
