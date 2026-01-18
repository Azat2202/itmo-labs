FROM nginx:1.27-alpine 
WORKDIR /usr/share/nginx/html
EXPOSE 8080
CMD ["nginx", "-g", "daemon off;"]
