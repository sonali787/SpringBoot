# Spring Security: Request Execution Flow

When you add Spring Security to a Spring Boot project, it automatically secures your application by establishing an end-to-end request processing flow. Here is how a request travels through this system under the hood:

---

### 🔄 Step-by-Step Request Lifecycle

#### 1. Interception at the Gateway (`springSecurityFilterChain`)
Every incoming HTTP request from a client is first intercepted by a Servlet Filter bean named `springSecurityFilterChain`. This filter chain sits directly between the client and the `DispatcherServlet`, ensuring that no request can bypass security checks.

#### 2. Authentication Challenge (The Login Form)
If the request is not yet authenticated:
* Spring Security interrupts the flow and redirects the client to a default, auto-configured login page located at `/login` (which presents a standard HTML login form), or prompts a Basic Auth pop-up.
* To authenticate, the user must provide the default username (`user`) and the randomly generated password printed to the console logs during application startup.

#### 3. Password Verification (BCrypt Hashing)
To verify the credentials securely without comparing plain text:
* Spring Security uses the `PasswordEncoder` interface under the hood.
* The system delegates verification to the `DelegatingPasswordEncoder`. It looks at the prefix identifier (e.g., `{bcrypt}`) on the stored password.
* It passes the user's input to `BCryptPasswordEncoder` which hashes it and compares it securely with the stored hash using a salt-aware comparison.

#### 4. Dispatching to the Controller
Once credentials are verified and the request is approved:
* The `springSecurityFilterChain` permits the request to pass through.
* The request is forwarded to the `DispatcherServlet`, which routes it to your controller (e.g., `HelloController` / `/hello`).

#### 5. Continuous Protection (CSRF & Security Headers)
During and after processing, Spring Security actively safeguards the session:
* Enforcing **CSRF (Cross-Site Request Forgery)** protection by default on all state-changing HTTP requests (POST, PUT, DELETE, PATCH).
* Appending default **security headers** (such as `X-Frame-Options` to prevent clickjacking, `X-XSS-Protection`, and `X-Content-Type-Options`).
