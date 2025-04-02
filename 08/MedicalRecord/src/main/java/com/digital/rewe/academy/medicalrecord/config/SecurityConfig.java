package com.digital.rewe.academy.medicalrecord.config;

import com.digital.rewe.academy.medicalrecord.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true
)
@AllArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

//    @Bean
//    public InMemoryUserDetailsManager userDetailsInMemory() {
//        UserDetails user1 = User
//            .withUsername("user1")
//            .password("$2a$12$VcsUDlmbLDczCbM2Cm5sAetkRVDSyHHzisAig1B.ypT/GW8XrcTq.")
//            .build();
//        UserDetails user2 = User.withUsername("user2")
//            .password("$2a$12$BUppzBU81H1M.doHy7GMBegE1SoVSbmM5BH1F/sd5MiVti1EXL6IG")
//            .build();
//        return new InMemoryUserDetailsManager(user1, user2);
//    }



    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests
//                (
//                    authz -> authz
//                        .requestMatchers("/api/medical-visits/**").hasAuthority("DOCTOR")
////                        .requestMatchers(HttpMethod.POST, "/api/recipes/**").hasAuthority("DOCTOR")
////                        .requestMatchers("/api/medicines/**").hasAuthority("SELLER")
////                        .requestMatchers(HttpMethod.GET, "/api/medicines").hasAuthority("CUSTOMER")
//                        .anyRequest().authenticated()
//                )
//            .csrf(csrf -> csrf.disable())
//            .httpBasic(Customizer.withDefaults())
//        ;
//        return http.build();
//    }


/*


@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests
            (
                authz -> authz
                    .requestMatchers("/api/addresses/**").hasAnyAuthority("ADMIN", "DOCTOR")
                    .requestMatchers(HttpMethod.GET, "/api/persons/{id}").hasAnyAuthority("ADMIN", "DOCTOR", "PATIENT")
                    .requestMatchers("/api/persons/**").hasAnyAuthority("ADMIN", "DOCTOR")
//                    .requestMatchers(HttpMethod.POST, "/api/persons").hasAnyAuthority("ADMIN", "DOCTOR", "PATIENT")
//                    .requestMatchers("/api/patients/**").hasAnyAuthority("ADMIN", "DOCTOR")
//                    .requestMatchers(HttpMethod.GET,"/api/patients/{id}").hasAnyAuthority("ADMIN", "DOCTOR", "PATIENT")
//                    .requestMatchers("/api/patients/diagnose/**").hasAuthority("ADMIN")
//                    .requestMatchers("/api/patients/count/**").hasAuthority("ADMIN")
//                    .requestMatchers("/api/doctors").hasAuthority("ADMIN")
//                    .requestMatchers(HttpMethod.GET, "/api/doctors/*").hasAnyAuthority("ADMIN", "DOCTOR")
//                    .requestMatchers(HttpMethod.PUT, "/api/doctors/*").hasAnyAuthority("ADMIN", "DOCTOR")
//                    .requestMatchers("/api/general-practitioners/**").hasAuthority("ADMIN")
//                    .requestMatchers(HttpMethod.GET, "/api/general-practitioners/*").hasAnyAuthority("ADMIN", "DOCTOR")
//                    .requestMatchers(HttpMethod.PUT, "/api/general-practitioners/*").hasAnyAuthority("ADMIN", "DOCTOR")
//                    .requestMatchers("/api/specialties/**").hasAuthority("ADMIN")
//                    .requestMatchers(HttpMethod.GET, "/api/specialties/*").hasAnyAuthority("ADMIN", "DOCTOR")
//                    .requestMatchers(HttpMethod.GET, "/api/specialties").hasAnyAuthority("ADMIN", "DOCTOR")
//                    .requestMatchers("/api/medical-visits/**").hasAuthority("ADMIN")
//                    .requestMatchers(HttpMethod.GET,"/api/medical-visits/*").hasAnyAuthority("ADMIN", "DOCTOR")
//                    .requestMatchers(HttpMethod.GET,"/api/medical-visits/period").hasAuthority("ADMIN")
//                    .requestMatchers(HttpMethod.GET,"/api/medical-visits/patient").hasAuthority("ADMIN")
//                    .requestMatchers(HttpMethod.PUT,"/api/medical-visits/*").hasAnyAuthority("ADMIN", "DOCTOR")
//                    .requestMatchers(HttpMethod.DELETE,"/api/medical-visits/*").hasAnyAuthority("ADMIN", "DOCTOR")
//                    .requestMatchers(HttpMethod.POST,"/api/medical-visits").hasAnyAuthority("ADMIN", "DOCTOR")
//                    .requestMatchers(HttpMethod.PATCH,"/api/medical-visits/*").hasAnyAuthority("ADMIN", "DOCTOR")
//                    .requestMatchers("/api/diagnoses/**").hasAuthority("ADMIN")
//                    .requestMatchers(HttpMethod.GET, "/api/diagnoses/**").hasAnyAuthority("ADMIN", "DOCTOR")
//                    .requestMatchers(HttpMethod.GET, "/api/diagnoses/rank").hasAuthority("ADMIN")
//                    .requestMatchers("/api/treatments/**").hasAuthority("ADMIN")
//                    .requestMatchers(HttpMethod.GET, "/api/treatments/*").hasAnyAuthority("ADMIN", "DOCTOR")
//                    .requestMatchers(HttpMethod.POST, "/api/treatments").hasAnyAuthority("ADMIN", "DOCTOR")
//                    .requestMatchers("/api/sick-leaves/**").hasAuthority("ADMIN")
//                    .requestMatchers(HttpMethod.POST, "/api/sick-leaves").hasAnyAuthority("ADMIN", "DOCTOR")


                    .anyRequest().authenticated()
//                    .requestMatchers("/api/medical-visits/**").hasAuthority("DOCTOR")
//                        .requestMatchers(HttpMethod.POST, "/api/recipes/**").hasAuthority("DOCTOR")
//                        .requestMatchers("/api/medicines/**").hasAuthority("SELLER")
//                        .requestMatchers(HttpMethod.GET, "/api/medicines").hasAuthority("CUSTOMER")
//                    .anyRequest().authenticated()
            )
        .csrf(csrf -> csrf.disable())
        .httpBasic(Customizer.withDefaults())
    ;
    return http.build();
}
*/

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests
            (
                authz -> authz
                    .requestMatchers("/api/addresses/**").hasAnyAuthority("ADMIN", "DOCTOR")
                    .requestMatchers(HttpMethod.POST, "/api/persons").hasAnyAuthority("ADMIN", "DOCTOR", "PATIENT")
                    .requestMatchers(HttpMethod.GET, "/api/persons/{id}").hasAnyAuthority("ADMIN", "DOCTOR", "PATIENT")
                    .requestMatchers("/api/persons/**").hasAnyAuthority("ADMIN", "DOCTOR")
                    .requestMatchers("/api/patients/count/**").hasAuthority("ADMIN")
                    .requestMatchers("/api/patients/diagnose/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET,"/api/patients/{id}").hasAnyAuthority("ADMIN", "DOCTOR", "PATIENT")
                    .requestMatchers("/api/patients/**").hasAnyAuthority("ADMIN", "DOCTOR")
                    .requestMatchers(HttpMethod.PUT, "/api/doctors/{id}").hasAnyAuthority("ADMIN", "DOCTOR")
                    .requestMatchers(HttpMethod.GET, "/api/doctors/{id}").hasAnyAuthority("ADMIN", "DOCTOR")
                    .requestMatchers(HttpMethod.GET, "/api/doctors/most-sick-leaves").hasAuthority("ADMIN")
                    .requestMatchers("/api/doctors/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/general-practitioners/{id}").hasAnyAuthority("ADMIN", "DOCTOR")
                    .requestMatchers(HttpMethod.GET, "/api/general-practitioners/{id}").hasAnyAuthority("ADMIN", "DOCTOR")
                    .requestMatchers("/api/general-practitioners/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/specialties/**").hasAnyAuthority("ADMIN", "DOCTOR")
                    .requestMatchers("/api/specialties/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.PATCH,"/api/medical-visits/*").hasAnyAuthority("ADMIN", "DOCTOR")
                    .requestMatchers(HttpMethod.POST,"/api/medical-visits").hasAnyAuthority("ADMIN", "DOCTOR")
                    .requestMatchers(HttpMethod.PUT,"/api/medical-visits/*").hasAnyAuthority("ADMIN", "DOCTOR")
                    .requestMatchers(HttpMethod.GET,"/api/medical-visits/patient").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET,"/api/medical-visits/period").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET,"/api/medical-visits/{id}").hasAnyAuthority("ADMIN", "DOCTOR", "PATIENT")
                    .requestMatchers("/api/medical-visits/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/diagnoses/rank").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/diagnoses/**").hasAnyAuthority("ADMIN", "DOCTOR")
                    .requestMatchers("/api/diagnoses/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/treatments").hasAnyAuthority("ADMIN", "DOCTOR")
                    .requestMatchers(HttpMethod.GET, "/api/treatments/**").hasAnyAuthority("ADMIN", "DOCTOR")
                    .requestMatchers("/api/treatments/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/sick-leaves").hasAnyAuthority("ADMIN", "DOCTOR")
                    .requestMatchers(HttpMethod.GET, "/api/sick-leaves/**").hasAnyAuthority("ADMIN", "DOCTOR")
                    .requestMatchers("/api/sick-leaves/**").hasAuthority("ADMIN")
                    .anyRequest().authenticated()
            )
        .csrf(csrf -> csrf.disable())
        .httpBasic(Customizer.withDefaults())
    ;
    return http.build();
}
}