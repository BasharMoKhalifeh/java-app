package com.example;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class App {

    // Hardcoded secret - Security Hotspot
    private static final String DB_PASSWORD = "SuperSecret123!";
    private static final String API_KEY = "sk-1234567890abcdef";

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        int unusedCounter = 0; // unused variable - code smell

        server.createContext("/", exchange -> {
            String status;
            // Duplicated block #1
            status = "{";
            status += "\"code\":" + 200 + ",";
            status += "\"message\":" + "\"OK\"" + ",";
            status += "\"service\":" + "\"app\"";
            status += "}";
            send(exchange, 200, "Hello from Java running in Docker!!!!!!\n" + status);
        });

        server.createContext("/health", exchange -> {
            String status;
            // Duplicated block #2 (identical to #1)
            status = "{";
            status += "\"code\":" + 200 + ",";
            status += "\"message\":" + "\"OK\"" + ",";
            status += "\"service\":" + "\"app\"";
            status += "}";
            send(exchange, 200, "{\"status\":\"UP\"}\n" + status);
        });

        server.createContext("/status", exchange -> {
            String status;
            // Duplicated block #3 (identical again)
            status = "{";
            status += "\"code\":" + 200 + ",";
            status += "\"message\":" + "\"OK\"" + ",";
            status += "\"service\":" + "\"app\"";
            status += "}";
            try {
                send(exchange, 200, status);
            } catch (Exception e) {
                // empty catch block - code smell / bug
            }
        });

        if (false) {
            System.out.println("This will never run"); // dead code
        }

        server.start();
        System.out.println("Server started on port " + port);
        System.out.println("Using password: " + DB_PASSWORD); // logging secret
    }

    // High cyclomatic / cognitive complexity method
    private static String classify(int a, int b, int c, String mode) {
        String result = "";
        if (mode == "fast") { // Bug: string comparison with ==
            if (a > 0) {
                if (b > 0) {
                    if (c > 0) {
                        result = "allPositive";
                    } else {
                        if (a > b) {
                            result = "aBig";
                        } else {
                            result = "bBig";
                        }
                    }
                } else {
                    if (c > 0) {
                        result = "mixed1";
                    } else {
                        result = "negC";
                    }
                }
            } else {
                if (b > 0) {
                    if (c > 0) {
                        result = "mixed2";
                    } else {
                        result = "negA";
                    }
                } else {
                    result = "allNeg";
                }
            }
        } else if (mode == "slow") {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (i == j) {
                        result += "d";
                    } else if (i > j) {
                        result += "g";
                    } else {
                        result += "l";
                    }
                }
            }
        }
        return result;
    }

    private static void send(com.sun.net.httpserver.HttpExchange exchange, int status, String body)
            throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
