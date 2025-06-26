from flask import Flask, jsonify, redirect, send_from_directory
from flask_cors import CORS
import pymysql
from config import Config
import logging

# 创建 Flask 应用实例
app = Flask(__name__)

# 加载配置
app.config.from_object(Config)

# 配置日志
logging.basicConfig(level=logging.INFO)
app.logger.setLevel(logging.INFO)

# 启用 CORS
CORS(app)

# 打印加载的配置以验证
app.logger.info("已加载配置:")
app.logger.info(f"MYSQL_HOST: {app.config.get('MYSQL_HOST')}")
app.logger.info(f"MYSQL_PORT: {app.config.get('MYSQL_PORT')}")
app.logger.info(f"MYSQL_USER: {app.config.get('MYSQL_USER')}")
app.logger.info(f"MYSQL_DB: {app.config.get('MYSQL_DB')}")

def get_db_connection():
    """创建并返回数据库连接"""
    try:
        # 验证所有必需的配置项都存在
        required_configs = ['MYSQL_HOST', 'MYSQL_PORT', 'MYSQL_USER', 'MYSQL_PASSWORD', 'MYSQL_DB']
        for config_key in required_configs:
            if config_key not in app.config:
                app.logger.error(f"缺少必需的配置项: {config_key}")
                return None
        
        app.logger.info(f"尝试连接数据库: {app.config['MYSQL_USER']}@{app.config['MYSQL_HOST']}:{app.config['MYSQL_PORT']}")
        
        # 创建数据库连接
        return pymysql.connect(
            host=app.config['MYSQL_HOST'],
            port=app.config['MYSQL_PORT'],
            user=app.config['MYSQL_USER'],
            password=app.config['MYSQL_PASSWORD'],
            db=app.config['MYSQL_DB'],
            charset='utf8mb4',
            cursorclass=pymysql.cursors.DictCursor
        )
    except pymysql.MySQLError as e:
        app.logger.error(f"数据库连接失败: {e}")
        return None
    except Exception as e:
        app.logger.error(f"创建数据库连接时发生意外错误: {e}")
        return None

# 添加静态文件路由
@app.route('/videos/<path:filename>')
def serve_video(filename):
    return send_from_directory('static/videos', filename)

@app.route('/')
def home():
    return redirect('/courses', code=302)

@app.route('/courses')
def get_courses():
    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "无法连接数据库"}), 500
    
    try:
        with conn.cursor() as cursor:
            cursor.execute('SELECT * FROM courses')
            courses = cursor.fetchall()
            return jsonify([dict(course) for course in courses])
    except Exception as e:
        app.logger.error(f"获取课程数据失败: {str(e)}")
        return jsonify({"error": "服务器内部错误"}), 500
    finally:
        conn.close()

@app.route('/videos/<int:course_id>')
def get_videos(course_id):
    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "无法连接数据库"}), 500
    
    try:
        with conn.cursor() as cursor:
            cursor.execute('SELECT * FROM videos WHERE course_id = %s', (course_id,))
            videos = cursor.fetchall()
            return jsonify([dict(video) for video in videos])
    except Exception as e:
        app.logger.error(f"获取视频数据失败: {str(e)}")
        return jsonify({"error": "服务器内部错误"}), 500
    finally:
        conn.close()

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)